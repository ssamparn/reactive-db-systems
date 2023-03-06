package com.reactive.database.r2dbcpostgresqlentityrelations.repository;

import com.reactive.database.r2dbcpostgresqlentityrelations.entity.CourseWork;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CourseWorkRepository extends ReactiveCrudRepository<CourseWork, Long> {
    Mono<Void> deleteByStudentId(Long studentId);
}
