package com.backend.demo.component.mapper;

import com.backend.demo.component.response.StudentResponse;
import com.backend.demo.repository.*;
import com.backend.demo.repository.entity.Degree;
import com.backend.demo.repository.entity.Student;
import com.backend.demo.repository.entity.Teacher;
import com.backend.demo.repository.enums.Role;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
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

    public StudentMapper(ProjectRepository projectRepository, FacultyRepository facultyRepository, CareerRepository careerRepository,
                         DegreeRepository degreeRepository, TeacherRepository teacherRepository, ModalityRepository modalityRepository) {
        this.projectRepository = projectRepository;
        this.facultyRepository = facultyRepository;
        this.modalityRepository = modalityRepository;
        this.careerRepository = careerRepository;
        this.degreeRepository = degreeRepository;
        this.teacherRepository = teacherRepository;
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
}
