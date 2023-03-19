package com.reactive.database.r2dbcpostgresqltodolist.web.controller;

import com.reactive.database.r2dbcpostgresqltodolist.mapper.ItemMapper;
import com.reactive.database.r2dbcpostgresqltodolist.service.ItemService;
import com.reactive.database.r2dbcpostgresqltodolist.web.model.event.Event;
import com.reactive.database.r2dbcpostgresqltodolist.web.model.request.NewItemResource;
import com.reactive.database.r2dbcpostgresqltodolist.web.model.request.UpdateItemResource;
import com.reactive.database.r2dbcpostgresqltodolist.web.model.response.ItemResource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping(value = "/items/create")
    public Mono<ResponseEntity<Void>> createItem(@Valid @RequestBody final NewItemResource newItem) {
        return itemService.createNewItem(itemMapper.toModel(newItem))
                .map(item -> created(linkTo(ItemController.class)
                        .slash(item.getId())
                        .toUri())
                        .build()
                );
    }

    @PutMapping(value = "/items/update/{itemId}")
    public Mono<ResponseEntity<Void>> update(@PathVariable(value = "itemId") @NotNull final Long itemId,
                                             @RequestHeader(value = HttpHeaders.IF_MATCH) final Long version,
                                             @Valid @RequestBody final UpdateItemResource itemToBeUpdated) {

        // Find the item and update the instance
        return itemService.findItemById(itemId, version, false)
                .map(item -> itemMapper.update(itemToBeUpdated, item))
                .flatMap(itemService::update)
                .map(item -> noContent().build());
    }

    @GetMapping(value = "/items/get/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ItemResource> findById(@PathVariable("itemId") final Long itemId) {
        return itemService.findItemById(itemId, null, true)
                .map(itemMapper::toResource);
    }

    @GetMapping(value = "/items/get/all", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ItemResource> getAllItems() {

        return itemService.findAllItems()
                .map(itemMapper::toResource);
    }

    @DeleteMapping(value = "/items/delete/{itemId}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("itemId") final Long itemId,
                                             @RequestHeader(value = HttpHeaders.IF_MATCH) final Long version) {

        return itemService.deleteById(itemId, version)
                .map(empty -> noContent().build());
    }

    @GetMapping("/items/events")
    public Flux<ServerSentEvent<Event>> listenToItemEvents() {
        // to be implemented
        return Flux.empty();
    }

}
