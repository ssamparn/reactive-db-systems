package com.reactive.database.r2dbcpostgresqlentityrelations.entity;

import jakarta.validation.constraints.NotNull;
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
@Table("course_work")
public class CourseWork {

    @Id
    private Long courseWorkId;

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;

    private Integer marks;

    public CourseWork(Long studentId, Long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }
}
