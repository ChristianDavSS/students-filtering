package com.backend.demo.service;

import com.backend.demo.component.dto.FacultyDto;
import com.backend.demo.component.mapper.FacultyMapper;
import com.backend.demo.repository.FacultyRepository;
import com.backend.demo.repository.entity.Faculty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;
    public FacultyService(FacultyRepository facultyRepository, FacultyMapper facultyMapper) {
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
    }

    public List<FacultyDto> findAll() {
        List<Faculty> facultyList = facultyRepository.findAll();
        return facultyList.stream().map(facultyMapper::toDto).toList();
    }
}
