package com.backend.demo.controller;

import com.backend.demo.component.dto.FacultyDto;
import com.backend.demo.service.FacultyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    // Dependency injection
    private final FacultyService facultyService;
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public List<FacultyDto> getAllFaculty() {
        return facultyService.findAll();
    }
}
