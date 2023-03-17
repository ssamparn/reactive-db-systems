package com.reactive.database.r2dbcpostgresqltodolist.db.repository;

import com.reactive.database.r2dbcpostgresqltodolist.db.entity.Tag;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface TagRepository extends R2dbcRepository<Tag, Long> {

}
