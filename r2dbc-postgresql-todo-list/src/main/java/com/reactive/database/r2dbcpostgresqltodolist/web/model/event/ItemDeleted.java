package com.reactive.database.r2dbcpostgresqltodolist.web.model.event;

import lombok.Value;

@Value
public class ItemDeleted implements Event {
    Long itemId;
}
