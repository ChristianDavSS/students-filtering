package com.backend.demo.component.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentRegisterRequest {
    private String id_number;
    private String name;
    private String generation;
    private Long facultyId;
    private Long careerId;
    private Long modalityId;
    private Date date;
    private Map<String, Long> teachers;
    private String project;
}
