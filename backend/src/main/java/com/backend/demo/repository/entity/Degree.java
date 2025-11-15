package com.backend.demo.repository.entity;

import com.backend.demo.repository.enums.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Degree")
public class Degree {
    @Id
    private String id;
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate date;

    // References
    private String modality;
    private String project;

    // Anidado
    private Map<Role, String> teachers;
}
