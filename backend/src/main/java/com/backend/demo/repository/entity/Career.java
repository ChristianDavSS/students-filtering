package com.backend.demo.repository.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Career")
public class Career {
    @Id
    private String id;
    private String name;

    private List<String> teachers;
}
