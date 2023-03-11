package com.reactive.database.r2dbcpostgresqlentityjoins.service;

import com.reactive.database.r2dbcpostgresqlentityjoins.entity.Employee;
import com.reactive.database.r2dbcpostgresqlentityjoins.repository.EmployeeRepository;
import com.reactive.database.r2dbcpostgresqlentityjoins.web.exception.EmployeeNotFoundException;
import com.reactive.database.r2dbcpostgresqlentityjoins.web.model.CreateEmployeeRequest;
import com.reactive.database.r2dbcpostgresqlentityjoins.web.model.UpdateEmployeeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Flux<Employee> getEmployees(String position, Boolean isFullTime) {

        if (position != null) {
            if (isFullTime != null) {
                return this.employeeRepository.findAllByPositionAndFullTime(position, isFullTime);
            } else {
                return this.employeeRepository.findAllByPosition(position);
            }
        } else {
            if (isFullTime != null) {
                return this.employeeRepository.findAllByFullTime(isFullTime);
            } else {
                return this.employeeRepository.findAll();
            }
        }
    }

    public Mono<Employee> getEmployee(Long id) {
        return this.employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException(id)));
    }

    public Mono<Employee> createEmployee(CreateEmployeeRequest employee) {
        return this.employeeRepository.save(
                Employee.builder()
                        .firstName(employee.firstName())
                        .lastName(employee.lastName())
                        .position(employee.position())
                        .fullTime(employee.isFullTime())
                        .build());
    }

    public Mono<Employee> updateEmployee(Long id, UpdateEmployeeRequest employee) {
        return this.employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException(id)))
                .flatMap(existingEmployee -> {
                    existingEmployee.setFirstName(employee.firstName());
                    existingEmployee.setLastName(employee.lastName());
                    existingEmployee.setPosition(employee.position());
                    existingEmployee.setFullTime(employee.isFullTime());
                    return this.employeeRepository.save(existingEmployee);
                });
    }

    public Mono<Void> deleteEmployee(Long id) {
        return this.employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException(id)))
                .flatMap(this.employeeRepository::delete)
                .then();
    }
}
