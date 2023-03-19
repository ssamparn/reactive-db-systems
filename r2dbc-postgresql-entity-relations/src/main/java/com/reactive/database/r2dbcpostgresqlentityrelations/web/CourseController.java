package com.reactive.database.r2dbcpostgresqlentityrelations.web;

import com.reactive.database.r2dbcpostgresqlentityrelations.entity.Course;
import com.reactive.database.r2dbcpostgresqlentityrelations.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class CourseController {

    private final CourseRepository courseRepository;

    @PostMapping(value = "/course/create")
    public Mono<ResponseEntity<Course>> addCourse(@RequestBody Course newCourse) {
        return courseRepository.save(newCourse)
                .map(course -> new ResponseEntity<>(course, HttpStatus.CREATED));
    }

    @GetMapping(value = "/course/get/{courseId}")
    public Mono<ResponseEntity<Course>> getCourse(@PathVariable("courseId") Long courseId) {
        return courseRepository.findById(courseId)
                .map(course -> new ResponseEntity<>(course, HttpStatus.OK));
    }

    @GetMapping(value = "/course/get/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Course> getCourses() {
        return courseRepository.findAll();
    }
}
