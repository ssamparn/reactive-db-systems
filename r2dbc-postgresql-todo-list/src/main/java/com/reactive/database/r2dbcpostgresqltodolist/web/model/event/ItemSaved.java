package com.reactive.database.r2dbcpostgresqltodolist.web.model.event;

import com.reactive.database.r2dbcpostgresqltodolist.web.model.response.ItemResource;
import lombok.Value;

@Value
public class ItemSaved implements Event {
    ItemResource item;
}
