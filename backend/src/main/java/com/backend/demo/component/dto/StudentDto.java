package com.backend.demo.component.dto;

import lombok.*;
import java.util.Date;
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
    private Date date;
    private String modality;
    private String project;
    private Map<String, String> teachers;
}
