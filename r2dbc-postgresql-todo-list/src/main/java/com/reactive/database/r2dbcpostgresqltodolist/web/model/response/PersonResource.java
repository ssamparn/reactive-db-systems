package com.reactive.database.r2dbcpostgresqltodolist.web.model.response;

import lombok.Data;

@Data
public class PersonResource {
    private Long id;
    private String firstName;
    private String lastName;
}
