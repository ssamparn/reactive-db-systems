package com.reactive.database.r2dbcpostgresqltodolist.web.model.response;

import com.reactive.database.r2dbcpostgresqltodolist.db.entity.ItemStatus;
import com.reactive.database.r2dbcpostgresqltodolist.db.entity.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class ItemResource {

    private Long id;
    private Long version;

    private String description;
    private ItemStatus status;

    private PersonResource assignee;
    private List<Tag> tags;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

}
