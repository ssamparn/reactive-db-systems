package com.reactive.database.r2dbcpostgresqltodolist.db.repository;

import com.reactive.database.r2dbcpostgresqltodolist.db.entity.Person;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends R2dbcRepository<Person, Long> {

}
