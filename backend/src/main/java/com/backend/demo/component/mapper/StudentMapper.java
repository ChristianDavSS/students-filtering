package com.backend.demo.component.mapper;

import com.backend.demo.component.response.StudentResponse;
import com.backend.demo.repository.*;
import com.backend.demo.repository.entity.Degree;
import com.backend.demo.repository.entity.Project;
import com.backend.demo.repository.entity.Student;
import com.backend.demo.repository.entity.Teacher;
import com.backend.demo.repository.enums.Role;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;

@Component
public class StudentMapper {
    private final ProjectRepository projectRepository;
    private final FacultyRepository facultyRepository;
    private final ModalityRepository modalityRepository;
    private final CareerRepository careerRepository;
    private final DegreeRepository degreeRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public StudentMapper(ProjectRepository projectRepository, FacultyRepository facultyRepository, CareerRepository careerRepository,
                         DegreeRepository degreeRepository, TeacherRepository teacherRepository, ModalityRepository modalityRepository,
                         StudentRepository studentRepository) {
        this.projectRepository = projectRepository;
        this.facultyRepository = facultyRepository;
        this.modalityRepository = modalityRepository;
        this.careerRepository = careerRepository;
        this.degreeRepository = degreeRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Method to make a Student object -> StudentResponse object.
     * Used for the GET methods.
     * */
    public StudentResponse toDto(Student student) {
        Degree degree = degreeRepository.findById(student.getDegreeId()).orElseThrow(()->
                new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "Not degree was found."));

        Map<Role, String> teachers = new HashMap<>();
        for (Map.Entry<Role, String> r : degree.getTeachers().entrySet()) {
            Teacher teacher = teacherRepository.findById(r.getValue()).orElseThrow(()->
                    new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "Not teacher found."));
            teachers.put(r.getKey(), teacher.getName());
        }

        return StudentResponse.builder()
                .studentId(student.getStudentId())
                .name(student.getName())
                .generation(student.getGeneration())
                .faculty(facultyRepository.findById(student.getFacultyId()).orElseThrow(()->
                        new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "No faculty was found")).getName())
                .career(careerRepository.findById(student.getCareerId()).orElseThrow(()->
                        new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "No career was found")).getName())
                .modality(modalityRepository.findById(degree.getModality()).orElseThrow(()->
                        new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "No modality was found")).getName())
                .date(degree.getDate())
                .project(projectRepository.findById(degree.getProject()).orElseThrow(()->
                        new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "No project was found")).getName())
                .teachers(teachers)
                .build();
    }

    /**
     * Method to delete a student by their ID. We also delete the project and degree if he´s the only one related.
     * */
    @Transactional
    public void deleteStudent(String studentId) {
        Student student = studentRepository.findByStudentId(studentId).orElseThrow(()->
                new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "Not student was found with the ID " + studentId));
        Degree degree = degreeRepository.findById(student.getDegreeId()).orElseThrow(()->
                new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "Not degree was found"));
        Project project = projectRepository.findById(degree.getProject()).orElseThrow(()->
                new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "Not project was found"));

        if (studentRepository.countStudentsByDegreeId(degree.getId()) <= 1) {
            if (degreeRepository.countDegreesByProject(degree.getId()) <= 1) {
                projectRepository.delete(project);
            }
            degreeRepository.delete(degree);
        }

        studentRepository.delete(student);
    }

}
