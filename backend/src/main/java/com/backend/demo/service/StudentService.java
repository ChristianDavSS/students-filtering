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

    /**
     * @param id, name, faculty, career: The user input may contain info like faculty, career, id_number, name, etc.
     *               If there´s no input, it return all the students.
     * @return List<StudentDto>: Returns a list of the students matching the filters.
     */
    public List<StudentDto> findAllByFilters(String id, String name, String faculty, String career) {
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

        return studentRepository.findAll(student).stream().map(studentMapper::toStudentDto).toList();
    }
}
