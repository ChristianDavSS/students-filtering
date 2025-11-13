package com.backend.demo.service;

import com.backend.demo.repository.*;
import com.backend.demo.repository.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ChartService {
    private final StudentRepository studentRepository;
    private final DegreeRepository degreeRepository;
    private final ProjectRepository projectRepository;

    public ChartService(StudentRepository studentRepository, DegreeRepository degreeRepository,
                        ProjectRepository projectRepository) {
        this.studentRepository = studentRepository;
        this.degreeRepository = degreeRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteStudentData(String id) {
        Student student = studentRepository.findById(id).orElseThrow(()->
            new ResponseStatusException(HttpStatus.CONFLICT, "Student not found")
        );

        if (studentRepository.countStudentsByDegree(student.getDegree()) <= 1) {
            degreeRepository.delete(student.getDegree());
            if (student.getDegree().getProject() != null &&
                    studentRepository.countStudentsByProject(student.getDegree().getProject()) <= 1) {
                projectRepository.delete(student.getDegree().getProject());
            }
        }

        studentRepository.delete(student);
    }
}
