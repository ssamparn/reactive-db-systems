package com.reactive.database.r2dbcpostgresqlentityrelations.repository;

import com.reactive.database.r2dbcpostgresqlentityrelations.entity.Student;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StudentRepository extends ReactiveCrudRepository<Student, Long> {
    @Query(value = "SELECT * FROM student WHERE (status = :status OR status is null) AND (student_name LIKE :name OR student_name is null) LIMIT :limit OFFSET :offset")
    Flux<Student> findAllByStatusAndStudentName(String status, String name, Long limit, Long offset);
}
