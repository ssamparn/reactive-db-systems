package com.reactive.database.r2dbcpostgresqlentityjoins.web.exception;

public class DepartmentNotFoundException extends RuntimeException{
    public DepartmentNotFoundException(Long id) {
        super(String.format("Department not found. Id: %d", id));
    }
}
