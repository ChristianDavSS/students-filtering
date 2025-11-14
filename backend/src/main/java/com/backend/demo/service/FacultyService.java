package com.backend.demo.service;

import com.backend.demo.repository.FacultyRepository;
import org.springframework.stereotype.Service;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }
}
