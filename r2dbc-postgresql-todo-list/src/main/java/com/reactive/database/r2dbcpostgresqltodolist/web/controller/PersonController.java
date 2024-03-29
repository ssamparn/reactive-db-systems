package com.reactive.database.r2dbcpostgresqltodolist.web.controller;

import com.reactive.database.r2dbcpostgresqltodolist.mapper.PersonMapper;
import com.reactive.database.r2dbcpostgresqltodolist.service.PersonService;
import com.reactive.database.r2dbcpostgresqltodolist.web.model.response.PersonResource;
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
public class PersonController {

    private final PersonService personService;
    private final PersonMapper personMapper;

    @GetMapping(value = "/people/get/all", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<PersonResource> getAll() {
        return personService.findAllPersons()
                .map(personMapper::toResource);
    }

    @GetMapping(value = "/people/get/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PersonResource> findById(@PathVariable("personId") final Long personId) {
        return personService.findPersonByPersonId(personId)
                .map(personMapper::toResource);
    }
}
