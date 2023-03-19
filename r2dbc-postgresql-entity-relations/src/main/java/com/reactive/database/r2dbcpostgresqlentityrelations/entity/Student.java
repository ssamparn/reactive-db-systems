package com.reactive.database.r2dbcpostgresqlentityrelations.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("student")
public class Student {

    @Id
    private Long studentId;

    private String studentName;

    @CreatedDate
    private LocalDateTime registeredOn;

    private StudentStatus status;
}
