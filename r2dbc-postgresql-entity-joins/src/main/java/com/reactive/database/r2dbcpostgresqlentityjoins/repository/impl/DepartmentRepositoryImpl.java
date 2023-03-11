package com.reactive.database.r2dbcpostgresqlentityjoins.repository.impl;

import com.reactive.database.r2dbcpostgresqlentityjoins.entity.Department;
import com.reactive.database.r2dbcpostgresqlentityjoins.entity.Employee;
import com.reactive.database.r2dbcpostgresqlentityjoins.repository.DepartmentRepository;
import com.reactive.database.r2dbcpostgresqlentityjoins.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final EmployeeRepository employeeRepository;
    private final DatabaseClient databaseClient;

    private static final String SELECT_QUERY = """
            SELECT d.id d_id, d.name d_name, m.id m_id, m.first_name m_firstName, m.last_name m_lastName,
                m.position m_position, m.is_full_time m_isFullTime, e.id e_id, e.first_name e_firstName,
                e.last_name e_lastName, e.position e_position, e.is_full_time e_isFullTime
            FROM departments d
            LEFT JOIN department_managers dm ON dm.department_id = d.id
            LEFT JOIN employees m ON m.id = dm.employee_id
            LEFT JOIN department_employees de ON de.department_id = d.id
            LEFT JOIN employees e ON e.id = de.employee_id
            """;

    @Override
    public Flux<Department> findAll() {
        String query = String.format("%s ORDER BY d.id", SELECT_QUERY);
        return databaseClient.sql(query)
                .fetch()
                .all()
                .bufferUntilChanged(result -> result.get("d_id"))
                .flatMap(Department::fromRows);
    }

    @Override
    public Mono<Department> findById(long id) {
        String query = String.format("%s WHERE d.id = :id", SELECT_QUERY);

        return databaseClient.sql(query)
                .bind("id", id)
                .fetch()
                .all()
                .bufferUntilChanged(result -> result.get("d_id"))
                .flatMap(Department::fromRows)
                .singleOrEmpty();
    }

    @Override
    public Mono<Department> findByName(String name) {
        String query = String.format("%s WHERE d.name = :name", SELECT_QUERY);

        return databaseClient.sql(query)
                .bind("name", name)
                .fetch()
                .all()
                .bufferUntilChanged(result -> result.get("d_id"))
                .flatMap(Department::fromRows)
                .singleOrEmpty();
    }

    @Override
    @Transactional
    public Mono<Department> save(Department department) {
        return this.saveDepartment(department)
                .flatMap(this::saveManager)
                .flatMap(this::saveEmployees)
                .flatMap(this::deleteDepartmentManager)
                .flatMap(this::saveDepartmentManager)
                .flatMap(this::deleteDepartmentEmployees)
                .flatMap(this::saveDepartmentEmployees);
    }

    @Override
    @Transactional
    public Mono<Void> delete(Department department) {
        return this.deleteDepartmentManager(department)
                .flatMap(this::deleteDepartmentEmployees)
                .flatMap(this::deleteDepartment)
                .then();
    }

    private Mono<Department> saveDepartment(Department department) {
        if (department.getId() == null) {
            return databaseClient.sql("INSERT INTO departments(name) VALUES(:name)")
                    .bind("name", department.getName())
                    .filter((statement, executeFunction) -> statement.returnGeneratedValues("id").execute())
                    .fetch().first()
                    .doOnNext(result -> department.setId(Long.parseLong(result.get("id").toString())))
                    .thenReturn(department);
        } else {
            return this.databaseClient.sql("UPDATE departments SET name = :name WHERE id = :id")
                    .bind("name", department.getName())
                    .bind("id", department.getId())
                    .fetch().first()
                    .thenReturn(department);
        }
    }

    private Mono<Department> saveManager(Department department) {
        return Mono.justOrEmpty(department.getManager())
                .flatMap(employeeRepository::save)
                .doOnNext(department::setManager)
                .thenReturn(department);
    }

    private Mono<Department> saveEmployees(Department department) {
        return Flux.fromIterable(department.getEmployees())
                .flatMap(this.employeeRepository::save)
                .collectList()
                .doOnNext(department::setEmployees)
                .thenReturn(department);
    }

    private Mono<Department> saveDepartmentManager(Department department) {
        String query = "INSERT INTO department_managers(department_id, employee_id) VALUES (:id, :empId)";

        return Mono.justOrEmpty(department.getManager())
                .flatMap(manager -> databaseClient.sql(query)
                        .bind("id", department.getId())
                        .bind("empId", manager.getId())
                        .fetch().rowsUpdated())
                .thenReturn(department);
    }

    private Mono<Department> saveDepartmentEmployees(Department department) {
        String query = "INSERT INTO department_employees(department_id, employee_id, employee_first_name) VALUES (:id, :empId, :empFirstName)";

        return Flux.fromIterable(department.getEmployees())
                .flatMap(employee -> databaseClient.sql(query)
                        .bind("id", department.getId())
                        .bind("empId", employee.getId())
                        .bind("empFirstName", employee.getFirstName())
                        .fetch().rowsUpdated())
                .collectList()
                .thenReturn(department);
    }

    private Mono<Void> deleteDepartment(Department department) {
        return databaseClient.sql("DELETE FROM departments WHERE id = :id")
                .bind("id", department.getId())
                .fetch().first()
                .then();
    }

    private Mono<Department> deleteDepartmentManager(Department department) {
        String query = "DELETE FROM department_managers WHERE department_id = :departmentId OR employee_id = :managerId";

        return Mono.just(department)
                .flatMap(dep -> databaseClient.sql(query)
                        .bind("departmentId", dep.getId())
                        .bindNull("managerId", Long.class)
                        .bind("managerId", dep.getManager().orElseGet(() -> Employee.builder().id(0L).build()).getId())
                        .fetch().rowsUpdated())
                .thenReturn(department);
    }

    private Mono<Department> deleteDepartmentEmployees(Department department) {
        String query = "DELETE FROM department_employees WHERE department_id = :id OR employee_id in (:ids)";

        List<Long> employeeIds = department.getEmployees().stream().map(Employee::getId).toList();

        return Mono.just(department)
                .flatMap(dep -> databaseClient.sql(query)
                        .bind("id", department.getId())
                        .bind("ids", employeeIds.isEmpty() ? List.of(0) : employeeIds)
                        .fetch().rowsUpdated())
                .thenReturn(department);
    }
}
