package com.backend.demo.service;

import com.backend.demo.repository.DegreeRepository;
import com.backend.demo.repository.entity.Degree;
import org.springframework.stereotype.Service;

@Service
public class DegreeService {
    private final DegreeRepository degreeRepository;
    public DegreeService(DegreeRepository degreeRepository) {
        this.degreeRepository = degreeRepository;
    }

    public void deleteDegree(Degree degree) {
        degreeRepository.delete(degree);
    }
}
