package com.reactive.database.r2dbcpostgresqlentityrelations.mapper;

import com.reactive.database.r2dbcpostgresqlentityrelations.entity.CourseWork;
import com.reactive.database.r2dbcpostgresqlentityrelations.entity.Student;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StudentMapper {

    public Collection<CourseWork> courseWorks(Student savedStudent) {
        return savedStudent.getCourses().stream()
                .map(course -> new CourseWork(savedStudent.getStudentId(), course.getCourseId()))
                .collect(Collectors.toSet());

    }
}
