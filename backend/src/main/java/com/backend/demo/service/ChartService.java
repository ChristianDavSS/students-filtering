package com.backend.demo.service;

import ch.qos.logback.core.util.StringUtil;
import com.backend.demo.repository.*;
import com.backend.demo.repository.entity.*;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChartService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final CareerRepository careerRepository;
    private final ModalityRepository modalityRepository;
    private final DegreeRepository degreeRepository;
    private final ProjectRepository projectRepository;

    public ChartService(StudentRepository studentRepository, FacultyRepository facultyRepository,
                        CareerRepository careerRepository, ModalityRepository modalityRepository, DegreeRepository degreeRepository,
                        ProjectRepository projectRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.careerRepository = careerRepository;
        this.modalityRepository = modalityRepository;
        this.degreeRepository = degreeRepository;
        this.projectRepository = projectRepository;
    }

    public Map<String, Long> getData(Long facultyId, Long careerId, String generation, Long modalityId) {
        Map<String, Long> map = new HashMap<>();
        List<Tuple> values = facultyRepository.countStudentsByFaculty();
        Faculty fac = null;
        Career car = null;

        if (facultyId != null) {
            fac = facultyRepository.findById(facultyId).orElse(null);
            values = careerRepository.countStudentsByCareer(fac);
        }
        if (careerId != null) {
            car = careerRepository.findById(careerId).orElse(null);
            values = studentRepository.countStudentsByGeneration(fac, car);
        }
        if (!StringUtil.isNullOrEmpty(generation)) {
            values = modalityRepository.countStudentsByModality(fac, car, generation);
        }
        if (modalityId != null) {
            Modality mod = modalityRepository.findById(modalityId).orElse(null);
            values = modalityRepository.countStudentsByAll(fac, car, generation, mod);
        }

        for (Tuple value : values) {
            map.put(value.get("name", String.class), value.get("quantity", Long.class));
        }

        return map;
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
