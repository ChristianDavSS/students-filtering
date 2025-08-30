package com.backend.demo.service;

import ch.qos.logback.core.util.StringUtil;
import com.backend.demo.component.dto.StudentDto;
import com.backend.demo.component.mapper.StudentMapper;
import com.backend.demo.repository.StudentRepository;
import com.backend.demo.repository.entity.Student;
import com.backend.demo.specification.StudentSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    // Dependency injection
    private final StudentRepository studentRepository;
    private final StudentSpecification studentSpecification;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository, StudentSpecification studentSpecification,
                          StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentSpecification = studentSpecification;
        this.studentMapper = studentMapper;
    }

    public List<StudentDto> findAllByFilters(String id, String name, String faculty, String career,
                                             String generation, String modality) {
        Specification<Student> student = Specification.allOf();

        if (!StringUtil.isNullOrEmpty(id)) {
            student = student.and(studentSpecification.hasId(id));
        }
        if (!StringUtil.isNullOrEmpty(name)) {
            student = student.and(studentSpecification.containsName(name));
        }
        if (!StringUtil.isNullOrEmpty(faculty)) {
            student = student.and(studentSpecification.hasFaculty(faculty));
        }
        if (!StringUtil.isNullOrEmpty(career)) {
            student = student.and(studentSpecification.hasCareer(career));
        }
        if (!StringUtil.isNullOrEmpty(generation)) {
            student = student.and(studentSpecification.hasGeneration(generation));
        }
        if (!StringUtil.isNullOrEmpty(modality)) {
            student = student.and(studentSpecification.hasModality(modality));
        }

        return studentRepository.findAll(student).stream().map(studentMapper::toStudentDto).toList();
    }
}
