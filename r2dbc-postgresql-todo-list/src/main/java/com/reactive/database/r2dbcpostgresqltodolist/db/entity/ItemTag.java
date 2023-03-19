package com.reactive.database.r2dbcpostgresqltodolist.db.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@Setter
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ItemTag {

   public ItemTag(Long itemId, Long tagId) {
      this.itemId = itemId;
      this.tagId = tagId;
   }

   @Id
   private Long id;

   @NotNull
   private Long itemId;

   @NotNull
   private Long tagId;

}
