package com.reactive.database.r2dbcpostgresqlentityjoins.web.exception;

public class DepartmentAlreadyExistsException extends RuntimeException {
    public DepartmentAlreadyExistsException(String name) {
        super(String.format("Department with name \"%s\" already exists.", name));
    }
}
