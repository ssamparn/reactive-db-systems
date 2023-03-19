package com.reactive.database.r2dbcpostgresqltodolist.mapper;

import com.reactive.database.r2dbcpostgresqltodolist.db.entity.ItemTag;
import com.reactive.database.r2dbcpostgresqltodolist.db.entity.Tag;
import com.reactive.database.r2dbcpostgresqltodolist.web.model.response.TagResource;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagResource toResource(Tag person);

    default Collection<Long> extractTagIdsFromTags(Collection<Tag> tags) {
        if(tags == null) {
            return new LinkedHashSet<>();
        }
        return tags.stream().map(Tag::getId).collect(Collectors.toSet());
    }

    default Collection<Long> extractTagIdsFromItemTags(Collection<ItemTag> itemTags) {
        if(itemTags == null) {
            return new LinkedHashSet<>();
        }
        return itemTags.stream().map(ItemTag::getTagId).collect(Collectors.toSet());
    }

    default Collection<ItemTag> toItemTagsFromItemIdAndTagIds(Long itemId, Collection<Tag> tags) {
        if (tags == null) {
            return new LinkedHashSet<>();
        }
        return tags.stream()
                .map(tag -> new ItemTag(itemId, tag.getId()))
                .collect(Collectors.toSet());
    }
}
