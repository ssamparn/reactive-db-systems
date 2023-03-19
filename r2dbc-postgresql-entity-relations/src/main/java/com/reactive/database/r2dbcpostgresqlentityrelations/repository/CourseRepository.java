package com.reactive.database.r2dbcpostgresqlentityrelations.repository;

import com.reactive.database.r2dbcpostgresqlentityrelations.entity.Course;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CourseRepository extends ReactiveCrudRepository<Course, Long> {

    @Query("select c.* from course c join course_work cw on c.course_id = cw.course_work_id where cw.student_id = :studentId order by c.course_name")
    Flux<Course> findCoursesByStudentId(Long studentId);
}
