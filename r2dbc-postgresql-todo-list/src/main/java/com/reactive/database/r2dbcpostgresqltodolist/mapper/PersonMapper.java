package com.reactive.database.r2dbcpostgresqltodolist.mapper;

import com.reactive.database.r2dbcpostgresqltodolist.db.entity.Person;
import com.reactive.database.r2dbcpostgresqltodolist.web.model.PersonResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonResource toResource(Person person);
}
