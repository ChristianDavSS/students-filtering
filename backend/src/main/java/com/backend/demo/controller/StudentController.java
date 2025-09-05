package com.backend.demo.controller;

import com.backend.demo.component.dto.StudentDto;
import com.backend.demo.component.request.StudentRegisterRequest;
import com.backend.demo.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {
    // Dependency injection
    private final StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/generation")
    public List<String> getGenerations() {
        return studentService.getGenerations();
    }

    @PostMapping
    public void registerStudent(@RequestBody StudentRegisterRequest request) {
        studentService.registerStudent(request);
    }

    @DeleteMapping("/{id}")
    public void deleteStudentById(@PathVariable String id) {
        studentService.deleteStudentById(id);
    }

    /**
     * @param id, name, faculty, career: class to get the faculty, career, name or id_number from a student to filter
     * @return List<StudentDto>: Returns a list of the dto of the users matching the filters
     */
    @GetMapping("/filter")
    public List<StudentDto> getStudentsByFilters(@RequestParam(required = false) String id,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) Long facultyId,
                                           @RequestParam(required = false) Long careerId,
                                           @RequestParam(required = false) String generation,
                                           @RequestParam(required = false) Long modalityId) {
        return studentService.findAllByFilters(id, name, facultyId, careerId, generation, modalityId);
    }

    @GetMapping("/chart")
    public Map<String, Long> getChartData(@RequestParam(required = false) Long facultyId,
                                         @RequestParam(required = false) Long careerId,
                                         @RequestParam(required = false) String generation,
                                          @RequestParam(required = false) Long modalityId){
        return studentService.getChartData(facultyId, careerId, generation, modalityId);
    }
}
