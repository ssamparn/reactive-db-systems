package com.reactive.database.r2dbcpostgresqltodolist.service;

import com.reactive.database.r2dbcpostgresqltodolist.db.entity.Item;
import com.reactive.database.r2dbcpostgresqltodolist.db.entity.ItemTag;
import com.reactive.database.r2dbcpostgresqltodolist.db.repository.ItemRepository;
import com.reactive.database.r2dbcpostgresqltodolist.db.repository.ItemTagRepository;
import com.reactive.database.r2dbcpostgresqltodolist.db.repository.PersonRepository;
import com.reactive.database.r2dbcpostgresqltodolist.db.repository.TagRepository;
import com.reactive.database.r2dbcpostgresqltodolist.exception.ItemNotFoundException;
import com.reactive.database.r2dbcpostgresqltodolist.exception.UnexpectedItemVersionException;
import com.reactive.database.r2dbcpostgresqltodolist.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.reactive.database.r2dbcpostgresqltodolist.db.entity.NotificationTopic.ITEM_DELETED;
import static com.reactive.database.r2dbcpostgresqltodolist.db.entity.NotificationTopic.ITEM_SAVED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Order.by("lastModifiedDate"));

    private final NotificationService notificationService;

    private final ItemRepository itemRepository;
    private final PersonRepository personRepository;
    private final ItemTagRepository itemTagRepository;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public Flux<Item> findAllItems() {
        return itemRepository.findAll(DEFAULT_SORT)
                .flatMap(this::loadRelations);
    }

    @Transactional
    public Mono<Item> createNewItem(Item item) {

        if (item.getId() != null || item.getVersion() != null) {
            return Mono.error(new IllegalArgumentException("When creating an item, the id and the version must be null"));
        }

        return itemRepository.save(item) //save the new item
                .flatMap(savedItem ->
                        itemTagRepository.saveAll(tagMapper.toItemTagsFromItemIdAndTagIds(savedItem.getId(), savedItem.getTags())) // Save the links to the tags
                                .collectList()
                                .then(Mono.just(savedItem))); // Return the newly created item
    }

    @Transactional
    public Mono<Item> update(Item itemToBeSaved) {
        if (itemToBeSaved.getId() == null || itemToBeSaved.getVersion() == null) {
            return Mono.error(new IllegalArgumentException("When updating an item, the id and the version must be provided"));
        }

        return verifyExistence(itemToBeSaved.getId())
                .then(itemTagRepository.findAllByItemId(itemToBeSaved.getId()).collectList()) // Find the existing link to the tags
                .flatMap(currentItemTags -> {
                    // As R2DBC does not support embedded IDs, the ItemTag entity has a technical key
                    // We can't just replace all ItemTags, we need to generate the proper insert/delete statements
                    Collection<Long> existingTagIds = tagMapper.extractTagIdsFromItemTags(currentItemTags);
                    final Collection<Long> tagIdsToSave = tagMapper.extractTagIdsFromTags(itemToBeSaved.getTags());

                    // Item Tags to be deleted
                    final Collection<ItemTag> toBeRemovedItemTags = currentItemTags.stream()
                            .filter(itemTag -> !tagIdsToSave.contains(itemTag.getTagId()))
                            .collect(Collectors.toList());

                    // Item Tags to be inserted
                    final Collection<ItemTag> toBeAddedItemTags = tagIdsToSave.stream()
                            .filter(tagId -> !existingTagIds.contains(tagId))
                            .map(tagId -> new ItemTag(itemToBeSaved.getId(), tagId))
                            .collect(Collectors.toList());

                    return itemTagRepository.deleteAll(toBeRemovedItemTags)
                            .then(itemTagRepository.saveAll(toBeAddedItemTags).collectList());
                })
                .then(itemRepository.save(itemToBeSaved));
    }

    public Mono<Item> findItemById(final Long itemId, final Long version, final boolean loadRelations) {
        final Mono<Item> itemMono =  itemRepository.findById(itemId)
                .switchIfEmpty(Mono.error(new ItemNotFoundException(itemId)))
                .handle((item, sink) -> {
                    // Optimistic locking: pre-check
                    if (version != null && !version.equals(item.getVersion())) {
                        sink.error(new UnexpectedItemVersionException(version, item.getVersion())); // The version are different, return an error
                    } else {
                        sink.next(item);
                    }
                });
        return loadRelations ? itemMono.flatMap(this::loadRelations) : itemMono; // Load the related objects, if requested
    }

    @Transactional
    public Mono<Void> deleteById(final Long id, final Long version) {
        return findItemById(id, version, false)
                .zipWith(itemTagRepository.deleteAllByItemId(id))
                .map(Tuple2::getT1)
                .flatMap(itemRepository::delete);
    }

    private Mono<Item> loadRelations(final Item item) {
        // Load the tags
        Mono<Item> mono = Mono.just(item)
                .zipWith(tagRepository.findTagsByItemId(item.getId()).collectList())
                .map(result -> result.getT1().setTags(result.getT2()));

        // Load the assignee (if set)
        if (item.getAssigneeId() != null) {
            mono = mono.zipWith(personRepository.findById(item.getAssigneeId()))
                    .map(result -> result.getT1().setAssignee(result.getT2()));
        }
        return mono;
    }

    private Mono<Boolean> verifyExistence(Long id) {
        return itemRepository.existsById(id)
                .handle((exists, sink) -> {
                    if (Boolean.FALSE.equals(exists)) {
                        sink.error(new ItemNotFoundException(id));
                    } else {
                        sink.next(exists);
                    }
                });
    }

    public Flux<Item> listenToSavedItems() {
        return this.notificationService.listen(ITEM_SAVED, Item.class)
                .flatMap(this::loadRelations);
    }

    public Flux<Long> listenToDeletedItems() {
        return this.notificationService.listen(ITEM_DELETED, Item.class)
                .map(Item::getId);
    }
}
