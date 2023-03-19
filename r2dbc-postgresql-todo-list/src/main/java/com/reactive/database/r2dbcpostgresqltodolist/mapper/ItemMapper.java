package com.reactive.database.r2dbcpostgresqltodolist.mapper;

import com.reactive.database.r2dbcpostgresqltodolist.db.entity.Item;
import com.reactive.database.r2dbcpostgresqltodolist.web.model.request.NewItemResource;
import com.reactive.database.r2dbcpostgresqltodolist.web.model.request.UpdateItemResource;
import com.reactive.database.r2dbcpostgresqltodolist.web.model.response.ItemResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PersonMapper.class, TagMapper.class})
public abstract class ItemMapper {

    public abstract Item toModel(NewItemResource itemResource);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "tags", ignore = true)
    public abstract Item update(UpdateItemResource itemToBeUpdated, @MappingTarget Item item);

    public abstract ItemResource toResource(Item item);

}
