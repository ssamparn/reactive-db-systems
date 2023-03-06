package com.reactive.database.r2dbcpostgresqlentityrelations.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse<T> {
    private boolean success;
    private String message;
    private Map<String, T> data;
}
