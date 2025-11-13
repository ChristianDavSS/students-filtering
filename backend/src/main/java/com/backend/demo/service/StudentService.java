package com.backend.demo.service;

import ch.qos.logback.core.util.StringUtil;
import com.backend.demo.component.dto.StudentDto;
import com.backend.demo.component.mapper.StudentMapper;
import com.backend.demo.component.mapper.StudentRegisterMapper;
import com.backend.demo.component.request.StudentRegisterRequest;
import com.backend.demo.repository.DegreeRepository;
import com.backend.demo.repository.ProjectRepository;
import com.backend.demo.repository.StudentRepository;
import com.backend.demo.repository.entity.Student;
import com.backend.demo.specification.StudentSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    // Dependency injection
    private final StudentRepository studentRepository;
    private final StudentSpecification studentSpecification;
    private final StudentMapper studentMapper;
    private final DegreeRepository degreeRepository;
    private final ProjectRepository projectRepository;
    private final StudentRegisterMapper studentRegisterMapper;

    public StudentService(StudentRepository studentRepository, StudentSpecification studentSpecification,
                          StudentMapper studentMapper, StudentRegisterMapper studentRegisterMapper,
                          DegreeRepository degreeRepository, ProjectRepository projectRepository) {
        this.studentRepository = studentRepository;
        this.studentSpecification = studentSpecification;
        this.studentMapper = studentMapper;
        this.degreeRepository = degreeRepository;
        this.projectRepository = projectRepository;
        this.studentRegisterMapper = studentRegisterMapper;
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
        return studentSpecification.filterBy(facultyId, careerId, generation, modalityId);
        //return chartService.getData(facultyId, careerId, generation, modalityId);
    }

    public void registerStudent(StudentRegisterRequest request) {
        studentRegisterMapper.registerStudent(request);
    }

    /**
     * Delete method for student:
     * This method checks if there´s more than one student on a project and on a degree to delete them on cascade.
     * */
    @Transactional(rollbackOn = Exception.class)
    public void deleteStudentById(String id) {
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
