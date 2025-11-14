package com.backend.demo.controller;

import com.backend.demo.component.request.StudentRegisterRequest;
import com.backend.demo.repository.entity.Student;
import com.backend.demo.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student registerStudent(@RequestBody StudentRegisterRequest request) {
        return this.studentService.registerStudent(request);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return this.studentService.getAllStudents();
    }
}
