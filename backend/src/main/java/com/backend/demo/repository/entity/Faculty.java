package com.backend.demo.repository.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Faculty")
public class Faculty {
    @Id
    private String id;
    private String name;

    // List with references to careers
    private List<String> careers;
}
