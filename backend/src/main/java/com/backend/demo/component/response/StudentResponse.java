package com.backend.demo.component.response;

import com.backend.demo.repository.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentResponse {
    private String studentId;
    private String name;
    private String generation;
    private String faculty;
    private String career;
    private String modality;
    private LocalDate date;
    private String project;
    private Map<Role, String> teachers;
}
