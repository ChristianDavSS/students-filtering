package com.backend.demo.service;

import com.backend.demo.repository.DegreeRepository;
import org.springframework.stereotype.Service;

@Service
public class DegreeService {
    private final DegreeRepository degreeRepository;

    public DegreeService(DegreeRepository degreeRepository) {
        this.degreeRepository = degreeRepository;
    }
}
