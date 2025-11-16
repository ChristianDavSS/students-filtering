package com.backend.demo.service;

import com.backend.demo.component.helpers.NameInterface;
import com.backend.demo.component.mapper.NameMapper;
import com.backend.demo.component.response.NameResponse;
import com.backend.demo.repository.CareerRepository;
import com.backend.demo.repository.FacultyRepository;
import com.backend.demo.repository.ModalityRepository;
import com.backend.demo.repository.StudentRepository;
import com.backend.demo.repository.entity.Student;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FiltersService {
    private final FacultyRepository facultyRepository;
    private final CareerRepository careerRepository;
    private final StudentRepository studentRepository;
    private  final ModalityRepository modalityRepository;
    private final NameMapper nameMapper;

    public FiltersService(FacultyRepository facultyRepository, CareerRepository careerRepository, StudentRepository studentRepository,
                          ModalityRepository modalityRepository, NameMapper nameMapper) {
        this.facultyRepository = facultyRepository;
        this.careerRepository = careerRepository;
        this.studentRepository = studentRepository;
        this.modalityRepository = modalityRepository;
        this.nameMapper = nameMapper;
    }

    public Map<String, List<NameResponse>> getFiltersData() {
        Map<String, List<NameResponse>> res = new HashMap<>();
        res.put("faculty", facultyRepository.findAll().stream().map(nameMapper::toDto).toList());
        res.put("career", careerRepository.findAll().stream().map(nameMapper::toDto).toList());
        res.put("modality", modalityRepository.findAll().stream().map(nameMapper::toDto).toList());
        res.put("generation", studentRepository.findAll().stream().map(v -> nameMapper.toDto(
                v.getGeneration()
        )).distinct().toList());

        return res;
    }
}
