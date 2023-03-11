package com.reactive.database.r2dbcpostgresqlentityjoins.web.controller;

import com.reactive.database.r2dbcpostgresqlentityjoins.entity.Department;
import com.reactive.database.r2dbcpostgresqlentityjoins.entity.Employee;
import com.reactive.database.r2dbcpostgresqlentityjoins.service.DepartmentService;
import com.reactive.database.r2dbcpostgresqlentityjoins.web.model.CreateDepartmentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping(value = "/departments", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Department> getDepartments() {
        return this.departmentService.getDepartments();
    }

    @GetMapping(value = "/departments/{departmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Department> getDepartment(@PathVariable("departmentId") Long departmentId) {
        return this.departmentService.getDepartment(departmentId);
    }

    @GetMapping("/departments/{departmentId}/employees")
    public Flux<Employee> getDepartmentEmployees(@PathVariable Long departmentId, @RequestParam(name = "fullTime", required = false) Boolean isFullTime) {
        return this.departmentService.getDepartmentEmployees(departmentId, isFullTime);
    }

    @PostMapping("/departments")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Department> createDepartment(@Valid @RequestBody CreateDepartmentRequest request) {
        return this.departmentService.createDepartment(request);
    }

    @PutMapping("/departments/{departmentId}")
    public Mono<Department> updateDepartment(@PathVariable("departmentId") Long departmentId, @RequestBody Department department) {
        return this.departmentService.updateDepartment(departmentId, department);
    }

    @DeleteMapping("/departments/{departmentId}")
    public Mono<Void> deleteDepartment(@PathVariable("departmentId") Long departmentId) {
        return this.departmentService.deleteDepartment(departmentId);
    }
}
