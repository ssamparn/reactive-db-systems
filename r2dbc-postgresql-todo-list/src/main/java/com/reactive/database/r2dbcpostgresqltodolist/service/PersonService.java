package com.reactive.database.r2dbcpostgresqltodolist.service;

import com.reactive.database.r2dbcpostgresqltodolist.db.entity.Person;
import com.reactive.database.r2dbcpostgresqltodolist.db.repository.PersonRepository;
import com.reactive.database.r2dbcpostgresqltodolist.exception.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PersonService {

    // the name of the fields to be sorted on are the DB field names
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Order.by("first_name,last_name"));

    private final PersonRepository personRepository;

    public Flux<Person> findAllPersons() {
        return personRepository.findAll(DEFAULT_SORT);
    }

    public Mono<Person> findPersonByItemId(final Long itemId) {
        return personRepository.findById(itemId)
                .switchIfEmpty(Mono.error(new ItemNotFoundException(itemId)));
    }
}
