package com.reactive.database.r2dbcpostgresqlentityrelations.repository;

import com.reactive.database.r2dbcpostgresqlentityrelations.entity.Course;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends ReactiveCrudRepository<Course, Long> {

}
