package com.reactive.database.r2dbcpostgresqlentityrelations.service;

import com.reactive.database.r2dbcpostgresqlentityrelations.entity.Course;
import com.reactive.database.r2dbcpostgresqlentityrelations.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public Mono<Course> addNewCourse(@RequestBody Course newCourse) {
        return courseRepository.save(newCourse);
    }

    public Mono<Course> getCourseById(@PathVariable("courseId") Long courseId) {
        return courseRepository.findById(courseId);
    }

    public Flux<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
