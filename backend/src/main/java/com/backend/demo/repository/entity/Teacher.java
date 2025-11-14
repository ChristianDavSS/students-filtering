package com.backend.demo.repository.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Teacher")
public class Teacher {
    @Id
    private String id;
    private String teacher_id;
    private String name;
}
