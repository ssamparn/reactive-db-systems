package com.reactive.database.r2dbcpostgresqltodolist.db.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Table
@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
public class Item {

    @Id
    private Long id;

    @Version
    private Long version;

    @Size(max=4000)
    @NotBlank
    private String description;

    @NotNull
    private ItemStatus status = ItemStatus.TODO;

    // personId: Id of the person who created the item
    private Long assigneeId;

    @Transient
    private Person assignee;

    @Transient
    private List<Tag> tags;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
