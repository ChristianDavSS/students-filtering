package com.backend.demo.service;

import com.backend.demo.component.dto.CareerDto;
import com.backend.demo.component.mapper.CareerMapper;
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
    private final CareerMapper careerMapper;
    public CareerService(CareerRepository careerRepository, FacultyRepository facultyRepository,
                         CareerMapper careerMapper) {
        this.careerRepository = careerRepository;
        this.facultyRepository = facultyRepository;
        this.careerMapper = careerMapper;
    }

    public List<CareerDto> getAllCareers(Long facultyId) {
        List<Career> careers = careerRepository.findAll();
        if (facultyId != null) {
            careers = careerRepository.getCareersByFaculty(
                    facultyRepository.findById(facultyId).orElse(null)
            );
        }

        return careers.stream().map(careerMapper::toDto).toList();
    }
}
