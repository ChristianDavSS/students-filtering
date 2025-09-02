package com.backend.demo.service;

import com.backend.demo.component.dto.NameDto;
import com.backend.demo.component.mapper.NameMapper;
import com.backend.demo.repository.FacultyRepository;
import com.backend.demo.repository.entity.Faculty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final NameMapper nameMapper;
    public FacultyService(FacultyRepository facultyRepository, NameMapper nameMapper) {
        this.facultyRepository = facultyRepository;
        this.nameMapper = nameMapper;
    }

    public List<NameDto> findAll() {
        List<Faculty> facultyList = facultyRepository.findAll();
        return facultyList.stream().map(nameMapper::toDto).toList();
    }
}
