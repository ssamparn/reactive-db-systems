package com.reactive.database.r2dbcpostgresqltodolist.web.model.event;

import lombok.Data;

import java.util.UUID;

@Data
public class HeartBeat implements Event {

    private String id = UUID.randomUUID().toString();

}
