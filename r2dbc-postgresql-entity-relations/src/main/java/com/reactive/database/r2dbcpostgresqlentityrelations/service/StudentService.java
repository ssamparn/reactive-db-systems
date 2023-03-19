package com.reactive.database.r2dbcpostgresqlentityrelations.service;

import com.reactive.database.r2dbcpostgresqlentityrelations.entity.Student;
import com.reactive.database.r2dbcpostgresqlentityrelations.mapper.StudentMapper;
import com.reactive.database.r2dbcpostgresqlentityrelations.repository.CourseWorkRepository;
import com.reactive.database.r2dbcpostgresqlentityrelations.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;
    private final CourseWorkRepository courseWorkRepository;

    @Transactional
    public Mono<Student> saveStudent(Student student) {
        if (student.getStudentId() != null) {
            return Mono.error(new IllegalArgumentException("When creating a student, the studentId must be null"));
        }
        return studentRepository.save(student)
                .flatMap(savedStudent -> courseWorkRepository.saveAll(studentMapper.courseWorks(savedStudent))
                        .collectList()
                .then(Mono.just(savedStudent)));
    }

    public Mono<Student> getStudentById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    public Flux<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional
    public Mono<Void> deleteStudentById(Long studentId) {
        return studentRepository.deleteById(studentId);
    }
}
