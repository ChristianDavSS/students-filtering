package com.backend.demo.service;

import com.backend.demo.component.dto.NameDto;
import com.backend.demo.component.mapper.NameMapper;
import com.backend.demo.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final NameMapper nameMapper;
    public TeacherService(TeacherRepository teacherRepository, NameMapper nameMapper) {
        this.teacherRepository = teacherRepository;
        this.nameMapper = nameMapper;
    }

    public List<NameDto> getAllTeachers() {
        return teacherRepository.findAll().stream().map(nameMapper::toDto).toList();
    }
}
