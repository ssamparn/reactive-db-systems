package com.reactive.database.r2dbcpostgresqlentityrelations.web;

import com.reactive.database.r2dbcpostgresqlentityrelations.entity.Student;
import com.reactive.database.r2dbcpostgresqlentityrelations.repository.CourseWorkRepository;
import com.reactive.database.r2dbcpostgresqlentityrelations.repository.StudentRepository;
import com.reactive.database.r2dbcpostgresqlentityrelations.web.response.GeneralResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class StudentsController {

    private final StudentRepository studentRepository;
    private final CourseWorkRepository courseWorkRepository;

    @PostMapping("/student")
    public Mono<ResponseEntity<Student>> addStudent(@RequestBody Student newStudent) {
        newStudent.setRegisteredOn(System.currentTimeMillis());
        newStudent.setStatus(1);

        return studentRepository.save(newStudent)
                .map(student -> new ResponseEntity<>(student, HttpStatus.CREATED));
    }

    @GetMapping("/student/{studentId}")
    public Mono<ResponseEntity<Student>> getStudent(@PathVariable Long studentId) {
        return studentRepository.findById(studentId)
                .map(student -> new ResponseEntity<>(student, HttpStatus.OK));
    }

    @GetMapping(value = "/students", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Student> getStudents(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Long limit,
                                     @RequestParam Map<String, String> filterParams) {
        String status = filterParams.getOrDefault("status", null);
        String name = filterParams.getOrDefault("name", null);
        if (name != null) {
            name = "%" + name + "%";
        }

        long offset = (page - 1) * limit;

        return studentRepository.findAllByStatusAndName(offset, limit, status, name).delayElements(Duration.ofSeconds(2L));
    }

    @PutMapping("/student/{studentId}")
    public Mono<ResponseEntity<GeneralResponse<Student>>> updateStudent(@PathVariable Long studentId, @RequestBody Student newStudentData) {
        return studentRepository.findById(studentId)
                .switchIfEmpty(Mono.error(new Exception("Student with ID " + studentId + " not found")))
                .flatMap(foundStudent -> {
                    foundStudent.setStudentName(newStudentData.getStudentName());
                    return studentRepository.save(foundStudent);
                }).map(updatedStudent -> {
                    HashMap<String, Student> data = new HashMap<>();
                    data.put("student", updatedStudent);
                    return new ResponseEntity<>(
                        GeneralResponse.<Student>builder()
                            .data(data)
                            .success(true)
                            .message("Student updated successfully")
                            .build(),
                        HttpStatus.ACCEPTED
                    );
                }).onErrorResume(e -> Mono.just(
                    new ResponseEntity<>(
                        GeneralResponse.<Student>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build(),
                        HttpStatus.NOT_FOUND)));
    }

    @Transactional
    @DeleteMapping("/student/{studentId}")
    Mono<ResponseEntity<GeneralResponse<Student>>> deleteStudent(@PathVariable Long studentId) {
        return studentRepository.findById(studentId)
                .switchIfEmpty(Mono.error(new Exception(String.format("Student with ID %d not found", studentId))))
                .flatMap(foundStudent -> courseWorkRepository.deleteByStudentId(studentId)
                        .then(studentRepository.deleteById(studentId))
                        .thenReturn(foundStudent))
                .map(deletedStudent -> {
                    HashMap<String, Student> data = new HashMap<>();
                    data.put("student", deletedStudent);

                    return new ResponseEntity<>(
                            GeneralResponse.<Student>builder()
                                    .success(true)
                                    .message("Student deleted successfully")
                                    .data(data)
                                    .build(),
                            HttpStatus.ACCEPTED
                    );
                })
                .onErrorResume(e -> Mono.just(
                        new ResponseEntity<>(
                                GeneralResponse.<Student>builder()
                                        .success(false)
                                        .message(e.getMessage())
                                        .build(),
                                HttpStatus.NOT_FOUND
                        )
                ));
    }

}
