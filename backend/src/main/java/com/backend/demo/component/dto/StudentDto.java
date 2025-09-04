package com.backend.demo.component.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private String id_number;
    private String name;
    private String generation;
    private String faculty;
    private String career;
    private LocalDate date;
    private String modality;
    private String project;
    private Map<String, String> teachers;
}
