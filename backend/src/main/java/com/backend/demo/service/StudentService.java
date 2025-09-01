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
    private final ChartService chartService;

    public StudentService(StudentRepository studentRepository, StudentSpecification studentSpecification,
                          StudentMapper studentMapper, ChartService chartService) {
        this.studentRepository = studentRepository;
        this.studentSpecification = studentSpecification;
        this.studentMapper = studentMapper;
        this.chartService = chartService;
    }

    public List<StudentDto> findAllByFilters(String id, String name, Long facultyId, Long careerId,
                                             String generation, Long modalityId) {
        Specification<Student> student = Specification.allOf();

        if (!StringUtil.isNullOrEmpty(id)) {
            student = student.and(studentSpecification.hasId(id));
        }
        if (!StringUtil.isNullOrEmpty(name)) {
            student = student.and(studentSpecification.containsName(name));
        }
        if (facultyId != null) {
            student = student.and(studentSpecification.hasFacultyId(facultyId));
        }
        if (careerId != null) {
            student = student.and(studentSpecification.hasCareerId(careerId));
        }
        if (!StringUtil.isNullOrEmpty(generation)) {
            student = student.and(studentSpecification.hasGeneration(generation));
        }
        if (modalityId != null) {
            student = student.and(studentSpecification.hasModalityId(modalityId));
        }

        return studentRepository.findAll(student).stream().map(studentMapper::toStudentDto).toList();
    }

    public List<String> getGenerations() {
        return studentRepository.findAll().stream().map(Student::getGeneration).distinct().toList();
    }

    public Map<String, Long> getChartData(Long facultyId, Long careerId, String generation, Long modalityId) {
        return chartService.getData(facultyId, careerId, generation, modalityId);
    }
}
