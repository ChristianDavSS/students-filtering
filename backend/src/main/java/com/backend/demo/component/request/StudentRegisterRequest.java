package com.backend.demo.component.request;

import com.backend.demo.repository.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class StudentRegisterRequest {
    private String studentId;
    private String name;
    private String generation;
    private String facultyId;
    private String careerId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    private String modalityId;
    private String project;
    private Map<Role, String> teachers;
}
