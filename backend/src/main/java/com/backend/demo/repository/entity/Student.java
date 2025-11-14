package com.backend.demo.repository.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Student")
public class Student {
    @Id
    private String id;
    private String studentId;
    private String name;
    private String generation;

    // References to another docs
    private String facultyId;
    private String careerId;
    private String degreeId;
}
