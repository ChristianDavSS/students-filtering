package com.backend.demo.component.mapper;

import com.backend.demo.component.request.StudentRegisterRequest;
import com.backend.demo.repository.*;
import com.backend.demo.repository.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class StudentRegisterMapper {
    private final StudentRepository studentRepository;
    private final CareerRepository careerRepository;
    private final FacultyRepository facultyRepository;
    private final DegreeRepository degreeRepository;
    private final DegreeTeacherRepository degreeTeacherRepository;
    private final RoleRepository roleRepository;
    private final TeacherRepository teacherRepository;
    private final ModalityRepository modalityRepository;
    private final ProjectRepository projectRepository;

    public StudentRegisterMapper(StudentRepository studentRepository, DegreeTeacherRepository degreeTeacherRepository,
                                 RoleRepository roleRepository, TeacherRepository teacherRepository, CareerRepository careerRepository,
                                 FacultyRepository facultyRepository, DegreeRepository degreeRepository,
                                 ModalityRepository modalityRepository, ProjectRepository projectRepository) {
        this.studentRepository = studentRepository;
        this.degreeTeacherRepository = degreeTeacherRepository;
        this.roleRepository = roleRepository;
        this.teacherRepository = teacherRepository;
        this.careerRepository = careerRepository;
        this.facultyRepository = facultyRepository;
        this.degreeRepository = degreeRepository;
        this.modalityRepository = modalityRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public void registerStudent(StudentRegisterRequest request) {
        if (studentRepository.findById(request.getId_number()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student already exists");
        }
        Project project;

        if (request.getProject() != null) {
            /* Creating or obtaining the Degree object */
            project = projectRepository.findByName(request.getProject()).orElseGet(() ->
                    projectRepository.save(
                            Project.builder().name(request.getProject()).build()
                    )
            );
        } else {
            project = null;
        }

        Degree degree = degreeRepository.findByAllData(request.getDate(),
                modalityRepository.findById(request.getModalityId()).orElse(null), project)
            .orElseGet(()->
                degreeRepository.save(
                    Degree
                        .builder()
                        .date(request.getDate())
                        .modality(
                            modalityRepository.findById(request.getModalityId()).orElseThrow(()->
                                    new ResponseStatusException(HttpStatus.CONFLICT, "Modality not found")
                            )
                        )
                        .project(project)
                        .build()
                )
            );

        /* Saving up the teachers */
        List<DegreeTeacher> degreeTeachers = new ArrayList<>();
        for (String role : request.getTeachers().keySet()) {
            degreeTeachers.add(
                DegreeTeacher
                    .builder()
                    .degreeTeacherId(
                        DegreeTeacherPK
                            .builder()
                            .degree_id(degree.getId())
                            .role_id(roleRepository.findByName(role).getId())
                            .teacher_id(Objects.requireNonNull(teacherRepository.findById(
                                    request.getTeachers().get(role)).orElse(null)).getId()
                            )
                            .build()
                    )
                    .degree(degree)
                    .teacher(teacherRepository.findById(request.getTeachers().get(role)).orElse(null))
                    .roles(roleRepository.findByName(role))
                    .build()
            );
        }
        degreeTeacherRepository.saveAll(degreeTeachers);


        studentRepository.save(
                Student
                    .builder()
                    .id(request.getId_number())
                    .name(request.getName())
                    .generation(request.getGeneration())
                    .career(
                        careerRepository.findById(request.getCareerId()).orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.CONFLICT, "Career not found")
                        )
                    )
                    .faculty(
                        facultyRepository.findById(request.getFacultyId()).orElseThrow(()->
                                new ResponseStatusException(HttpStatus.CONFLICT, "Faculty not found")
                        )
                    )
                    .degree(degree)
                    .build()
        );
    }
}
