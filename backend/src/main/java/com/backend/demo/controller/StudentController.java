package com.backend.demo.controller;

import com.backend.demo.component.request.StudentRegisterRequest;
import com.backend.demo.component.response.StudentResponse;
import com.backend.demo.repository.entity.Student;
import com.backend.demo.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public List<StudentResponse> getAllStudents() {
        return this.studentService.getAllStudents();
    }

    @GetMapping("/chart")
    public Map<String, Long> getChartData(@RequestParam(required = false, name = "facultyId") String facultyId,
                                          @RequestParam(required = false, name = "careerId") String careerId,
                                          @RequestParam(required = false, name = "generation") String generation,
                                          @RequestParam(required = false, name = "modalityId") String modalityId) {
        return studentService.getChartData(facultyId, careerId, generation, modalityId);
    }

    @DeleteMapping
    public void deleteStudent(@RequestParam("studentId") String studentId) {
        studentService.deleteStudent(studentId);
    }
}
