package com.backend.demo.component.mapper;

import com.backend.demo.component.dto.TeacherDto;
import com.backend.demo.repository.entity.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {
    public TeacherDto toDto(Teacher teacher) {
        return TeacherDto
                .builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .build();
    }
}
