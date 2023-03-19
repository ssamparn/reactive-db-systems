package com.reactive.database.r2dbcpostgresqltodolist.service;

import com.reactive.database.r2dbcpostgresqltodolist.db.entity.Tag;
import com.reactive.database.r2dbcpostgresqltodolist.db.repository.TagRepository;
import com.reactive.database.r2dbcpostgresqltodolist.exception.TagNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TagService {

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Order.by("name"));

    private final TagRepository tagRepository;

    public Flux<Tag> findAllTags() {
        return tagRepository.findAll(DEFAULT_SORT);
    }

    public Mono<Tag> findTagByTagId(final Long tagId) {

        return tagRepository.findById(tagId)
                .switchIfEmpty(Mono.error(new TagNotFoundException(tagId)));
    }

}
