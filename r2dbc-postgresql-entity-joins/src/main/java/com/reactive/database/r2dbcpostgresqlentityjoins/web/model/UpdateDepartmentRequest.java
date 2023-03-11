package com.reactive.database.r2dbcpostgresqlentityjoins.web.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateDepartmentRequest(
        @NotNull(message = "Name can not be null")
        @NotEmpty(message = "Name can not be empty")
        String name,
        @NotNull(message = "Name can not be null")
        @NotEmpty(message = "Name can not be empty")
        UpdateEmployeeRequest employee) {
}
