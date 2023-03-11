package com.reactive.database.r2dbcpostgresqlentityjoins.service;

import com.reactive.database.r2dbcpostgresqlentityjoins.entity.Department;
import com.reactive.database.r2dbcpostgresqlentityjoins.entity.Employee;
import com.reactive.database.r2dbcpostgresqlentityjoins.repository.DepartmentRepository;
import com.reactive.database.r2dbcpostgresqlentityjoins.web.exception.DepartmentAlreadyExistsException;
import com.reactive.database.r2dbcpostgresqlentityjoins.web.exception.DepartmentNotFoundException;
import com.reactive.database.r2dbcpostgresqlentityjoins.web.model.CreateDepartmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public Flux<Department> getDepartments() {
        return this.departmentRepository.findAll();
    }

    public Mono<Department> getDepartment(Long id) {
        return this.departmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new DepartmentNotFoundException(id)));
    }

    public Flux<Employee> getDepartmentEmployees(Long id, Boolean isFullTime) {
        if (isFullTime != null) {
            return this.departmentRepository.findById(id)
                    .switchIfEmpty(Mono.error(new DepartmentNotFoundException(id)))
                    .flatMapMany(department ->
                            Flux.fromStream(department.getEmployees()
                                    .stream()
                                    .filter(employee -> employee.isFullTime() == isFullTime)));
        } else {
            return this.departmentRepository.findById(id)
                    .switchIfEmpty(Mono.error(new DepartmentNotFoundException(id)))
                    .flatMapMany(department -> Flux.fromIterable(department.getEmployees()));
        }
    }

    public Mono<Department> createDepartment(CreateDepartmentRequest request) {
        return this.departmentRepository.findByName(request.name())
                .flatMap(department -> Mono.error(new DepartmentAlreadyExistsException(department.getName())))
                .defaultIfEmpty(Department.builder().name(request.name()).build()).cast(Department.class)
                .flatMap(this.departmentRepository::save);
    }

    public Mono<Department> updateDepartment(Long id, Department department) {
        return this.departmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new DepartmentNotFoundException(id)))
                .doOnNext(currentDepartment -> {
                    currentDepartment.setName(department.getName());

                    if(department.getManager().isPresent()){
                        currentDepartment.setManager(department.getManager().get());
                    }

                    currentDepartment.setEmployees(department.getEmployees());
                })
                .flatMap(this.departmentRepository::save);
    }

    public Mono<Void> deleteDepartment(Long id) {
        return this.departmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new DepartmentNotFoundException(id)))
                .flatMap(this.departmentRepository::delete)
                .then();
    }

}
