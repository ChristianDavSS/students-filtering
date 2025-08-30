package com.backend.demo.controller;

import com.backend.demo.component.dto.StudentDto;
import com.backend.demo.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    // Dependency injection
    private final StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * @param id, name, faculty, career: class to get the faculty, career, name or id_number from a student to filter
     * @return List<StudentDto>: Returns a list of the dto of the users matching the filters
     */
    @GetMapping
    public List<StudentDto> getAllStudents(@RequestParam(required = false) String id,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) String faculty,
                                           @RequestParam(required = false) String career,
                                           @RequestParam(required = false) String generation,
                                           @RequestParam(required = false) String modality) {
        return studentService.findAllByFilters(id, name, faculty, career, generation, modality);
    }
}
