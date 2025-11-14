package com.backend.demo.service;

import com.backend.demo.component.helpers.StudentRegisterHelper;
import com.backend.demo.component.request.StudentRegisterRequest;
import com.backend.demo.repository.StudentRepository;
import com.backend.demo.repository.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentRegisterHelper studentRegisterHelper;

    public StudentService(StudentRepository studentRepository, StudentRegisterHelper studentRegisterHelper) {
        this.studentRepository = studentRepository;
        this.studentRegisterHelper = studentRegisterHelper;
    }

    /**
     * Method to register a new student.
     * */
    public Student registerStudent(StudentRegisterRequest request) {
        return studentRegisterHelper.registerStudent(request);
    }

    /**
     * Method to get all the students.
     * To Do: Make it pageable to improve the performance
     * */
    public List<Student> getAllStudents() {
        return this.studentRepository.findAll();
    }
}
