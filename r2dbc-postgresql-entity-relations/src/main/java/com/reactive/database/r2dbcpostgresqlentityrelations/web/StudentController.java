package com.reactive.database.r2dbcpostgresqlentityrelations.web;

import com.reactive.database.r2dbcpostgresqlentityrelations.entity.Student;
import com.reactive.database.r2dbcpostgresqlentityrelations.entity.StudentStatus;
import com.reactive.database.r2dbcpostgresqlentityrelations.repository.CourseWorkRepository;
import com.reactive.database.r2dbcpostgresqlentityrelations.repository.StudentRepository;
import com.reactive.database.r2dbcpostgresqlentityrelations.service.StudentService;
import com.reactive.database.r2dbcpostgresqlentityrelations.web.response.GeneralResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class StudentController {

    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final CourseWorkRepository courseWorkRepository;

    @PostMapping("/student/create")
    public Mono<ResponseEntity<Student>> addStudent(@RequestBody Student newStudent) {
        newStudent.setStatus(StudentStatus.NEW);
        return studentService.saveStudent(newStudent)
                .map(student -> new ResponseEntity<>(student, HttpStatus.CREATED));
    }

    @GetMapping("/student/get/{studentId}")
    public Mono<ResponseEntity<Student>> getStudent(@PathVariable Long studentId) {
        return studentService.getStudentById(studentId)
                .map(student -> new ResponseEntity<>(student, HttpStatus.OK));
    }

    @GetMapping(value = "/students/get/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Student> getStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping(value = "/students/get", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Student> getStudents(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Long limit,
                                     @RequestParam Map<String, String> filterParams) {
        // Needs work. Pagination is not working properly with different conditions
        String status = filterParams.getOrDefault("status", null);
        String name = filterParams.getOrDefault("name", null);
        if (name != null) {
            name = "%" + name + "%";
        }

        long offset = (page - 1) * limit;

        return studentRepository.findAllByStatusAndStudentName(status, name, limit, offset);
    }

    @PutMapping("/student/update/{studentId}")
    public Mono<ResponseEntity<GeneralResponse<Student>>> updateStudent(@PathVariable Long studentId, @RequestBody Student newStudentData) {
        return studentService.getStudentById(studentId)
                .switchIfEmpty(Mono.error(new Exception("Student with ID " + studentId + " not found")))
                .flatMap(foundStudent -> {
                    foundStudent.setStudentName(newStudentData.getStudentName());
                    return studentService.saveStudent(foundStudent);
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
    @DeleteMapping("/student/delete/{studentId}")
    Mono<ResponseEntity<GeneralResponse<Student>>> deleteStudent(@PathVariable Long studentId) {
        return studentService.getStudentById(studentId)
                .switchIfEmpty(Mono.error(new Exception(String.format("Student with ID %d not found", studentId))))
                .flatMap(foundStudent -> courseWorkRepository.deleteByStudentId(studentId)
                        .then(studentService.deleteStudentById(studentId))
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
