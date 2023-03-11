package com.reactive.database.r2dbcpostgresqlentityjoins.web.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super(String.format("Employee not found. Id: %d", id));
    }
}
