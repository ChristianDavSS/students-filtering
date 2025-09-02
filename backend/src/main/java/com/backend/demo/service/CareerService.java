package com.backend.demo.service;

import com.backend.demo.component.dto.NameDto;
import com.backend.demo.component.mapper.NameMapper;
import com.backend.demo.repository.CareerRepository;
import com.backend.demo.repository.FacultyRepository;
import com.backend.demo.repository.entity.Career;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareerService {
    // Dependency injection
    private final CareerRepository careerRepository;
    private final FacultyRepository facultyRepository;
    private final NameMapper nameMapper;
    public CareerService(CareerRepository careerRepository, FacultyRepository facultyRepository,
                         NameMapper nameMapper) {
        this.careerRepository = careerRepository;
        this.facultyRepository = facultyRepository;
        this.nameMapper = nameMapper;
    }

    public List<NameDto> getAllCareers(Long facultyId) {
        List<Career> careers = careerRepository.findAll();
        if (facultyId != null) {
            careers = careerRepository.getCareersByFaculty(
                    facultyRepository.findById(facultyId).orElse(null)
            );
        }

        return careers.stream().map(nameMapper::toDto).toList();
    }
}
