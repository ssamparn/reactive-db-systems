package com.reactive.database.r2dbcpostgresqlentityjoins.web.controller;

import com.reactive.database.r2dbcpostgresqlentityjoins.entity.Employee;
import com.reactive.database.r2dbcpostgresqlentityjoins.service.EmployeeService;
import com.reactive.database.r2dbcpostgresqlentityjoins.web.model.CreateEmployeeRequest;
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
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Employee> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        return this.employeeService.createEmployee(request);
    }

    @GetMapping("/employees")
    public Flux<Employee> getEmployees(@RequestParam(required = false) String position,
                                       @RequestParam(name = "fullTime", required = false) Boolean isFullTime) {
        return this.employeeService.getEmployees(position, isFullTime);
    }

    @GetMapping(value = "/employees/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Employee> getEmployee(@PathVariable("employeeId") Long employeeId) {
        return this.employeeService.getEmployee(employeeId);
    }

    @PutMapping("/employees/{employeeId}")
    public Mono<Employee> updateEmployee(@PathVariable("employeeId") Long employeeId, Employee employee) {
        return this.employeeService.updateEmployee(employeeId, employee);
    }

    @DeleteMapping("/employees/{employeeId}")
    public Mono<Void> deleteEmployee(@PathVariable Long employeeId) {
        return this.employeeService.deleteEmployee(employeeId);
    }
}
