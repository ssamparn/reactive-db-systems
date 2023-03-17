package com.reactive.database.r2dbcpostgresqltodolist.mapper;

import com.reactive.database.r2dbcpostgresqltodolist.db.entity.Tag;
import com.reactive.database.r2dbcpostgresqltodolist.web.model.response.TagResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagResource toResource(Tag person);
}
