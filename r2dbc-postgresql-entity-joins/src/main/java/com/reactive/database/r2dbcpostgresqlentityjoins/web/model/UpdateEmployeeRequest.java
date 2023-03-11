package com.reactive.database.r2dbcpostgresqlentityjoins.web.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateEmployeeRequest(@NotNull @NotEmpty String firstName,
                                    @NotNull @NotEmpty String lastName,
                                    @NotNull @NotEmpty String position,
                                    @NotNull boolean isFullTime) {
}
