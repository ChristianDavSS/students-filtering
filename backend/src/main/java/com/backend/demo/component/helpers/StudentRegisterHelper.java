package com.backend.demo.component.helpers;

import com.backend.demo.component.request.StudentRegisterRequest;
import com.backend.demo.repository.DegreeRepository;
import com.backend.demo.repository.ModalityRepository;
import com.backend.demo.repository.ProjectRepository;
import com.backend.demo.repository.StudentRepository;
import com.backend.demo.repository.entity.Degree;
import com.backend.demo.repository.entity.Modality;
import com.backend.demo.repository.entity.Project;
import com.backend.demo.repository.entity.Student;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

@Component
public class StudentRegisterHelper {
    private final ModalityRepository modalityRepository;
    private final ProjectRepository projectRepository;
    private final DegreeRepository degreeRepository;
    private final StudentRepository studentRepository;

    public StudentRegisterHelper(ModalityRepository modalityRepository, ProjectRepository projectRepository,
                                 DegreeRepository degreeRepository, StudentRepository studentRepository) {
        this.modalityRepository = modalityRepository;
        this.projectRepository = projectRepository;
        this.degreeRepository = degreeRepository;
        this.studentRepository = studentRepository;
    }

    public Student registerStudent(StudentRegisterRequest request) {
        // If there´s already a student with their ID we throw an error.
        if (studentRepository.existsByStudentId(request.getStudentId())) {
            throw new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "Student already registered");
        }

        // Obtain the modality and the project by their id and name.
        Modality modality = modalityRepository.findById(request.getModalityId()).orElseThrow(()->
                new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "Error en los datos"));
        Project project = projectRepository.findByName(request.getProject()).orElseGet(()->
                projectRepository.save(Project.builder()
                        .name(request.getProject())
                        .build()
                ));

        // We look if there´s any other degree with the exact same data
        Degree degree = degreeRepository.findDegreeByDateAndModalityAndProjectAndTeachers(
                request.getDate(), modality.getId(), project.getId(), request.getTeachers()
        ).orElseGet(()->
                degreeRepository.save(Degree.builder()
                        .date(request.getDate())
                        .modality(modality.getId())
                        .project(project.getId())
                        .teachers(request.getTeachers())
                        .build()
                ));

        // Return the student created
        return studentRepository.save(
                Student.builder()
                        .studentId(request.getStudentId())
                        .name(request.getName())
                        .generation(request.getGeneration())
                        .facultyId(request.getFacultyId())
                        .careerId(request.getCareerId())
                        .degreeId(degree.getId())
                        .build()
        );
    }
}
