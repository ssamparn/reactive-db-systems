package com.reactive.database.r2dbcpostgresqltodolist.web.controller;

import com.reactive.database.r2dbcpostgresqltodolist.mapper.TagMapper;
import com.reactive.database.r2dbcpostgresqltodolist.service.TagService;
import com.reactive.database.r2dbcpostgresqltodolist.web.model.response.TagResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping(value = "/tags/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TagResource> findById(@PathVariable final Long id) {

        return tagService.findById(id)
                .map(tagMapper::toResource);
    }

    @GetMapping(value = "/tags/get-all", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<TagResource> getAll() {

        return tagService.findAll()
                .map(tagMapper::toResource);
    }
}
