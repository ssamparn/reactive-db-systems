package com.reactive.database.r2dbcpostgresqlentityrelations.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("course")
public class Course {

    @Id
    private Long courseId;

    @NotBlank
    @Size(max=20)
    private String courseName;
}
