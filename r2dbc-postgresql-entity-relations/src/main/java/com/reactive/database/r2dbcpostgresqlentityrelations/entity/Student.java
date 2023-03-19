package com.reactive.database.r2dbcpostgresqlentityrelations.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("student")
public class Student {

    @Id
    private Long studentId;

    @Size(max=40)
    @NotBlank
    private String studentName;

    @CreatedDate
    private LocalDateTime registeredOn;

    private StudentStatus status;

    @Transient
    private List<Course> courses;
}
