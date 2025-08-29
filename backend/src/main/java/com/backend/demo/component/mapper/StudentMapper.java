package com.backend.demo.component.mapper;

import com.backend.demo.component.dto.StudentDto;
import com.backend.demo.repository.entity.DegreeTeacher;
import com.backend.demo.repository.entity.Project;
import com.backend.demo.repository.entity.Student;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StudentMapper {
    /**
     * @param student: Student instance to convert it into the Dto
     * @return StudentDto: The student Dto.
     */
    public StudentDto toStudentDto(Student student) {
        Project project = student.getDegree().getProject();
        // Verifying there´s a project on this student.
        String projectName = null;
        if (project != null) projectName = project.getName();

        // Creating a map for the teachers and their role
        Map<String, String> teachers = new HashMap<>();
        for (DegreeTeacher teacher : student.getDegree().getDegreeTeachers()) {
            teachers.put(teacher.getRoles().getName().toLowerCase().replace(" ", "_"),
                    teacher.getTeacher().getName());
        }

        // Create the DTO and return it
        return StudentDto
                .builder()
                .id_number(student.getId())
                .name(student.getName())
                .generation(student.getGeneration())
                .faculty(student.getFaculty().getName())
                .career(student.getCareer().getName())
                .date(student.getDegree().getDate())
                .modality(student.getDegree().getModality().getName())
                .project(projectName)
                .teachers(teachers)
                .build();
    }
}
