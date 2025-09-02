package com.backend.demo.service;

import com.backend.demo.component.dto.TeacherDto;
import com.backend.demo.component.mapper.TeacherMapper;
import com.backend.demo.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    public TeacherService(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
    }

    public List<TeacherDto> getAllTeachers() {
        return teacherRepository.findAll().stream().map(teacherMapper::toDto).toList();
    }
}
