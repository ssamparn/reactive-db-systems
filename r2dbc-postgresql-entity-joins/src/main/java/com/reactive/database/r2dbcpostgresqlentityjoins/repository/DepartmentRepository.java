package com.reactive.database.r2dbcpostgresqlentityjoins.repository;

import com.reactive.database.r2dbcpostgresqlentityjoins.entity.Department;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public interface DepartmentRepository {
    Flux<Department> findAll();

    Mono<Department> findById(long id);

    Mono<Department> findByName(String name);

    Mono<Department> save(Department department);

    Mono<Void> delete(Department department);
}
