package com.backend.demo.component.response;

import com.backend.demo.repository.enums.Role;

import java.util.Date;
import java.util.Map;

public class StudentResponse {
    private String studentId;
    private String name;
    private String generation;
    private String faculty;
    private String career;
    private String modality;
    private Date date;
    private String project;
    private Map<Role, String> teachers;
}
