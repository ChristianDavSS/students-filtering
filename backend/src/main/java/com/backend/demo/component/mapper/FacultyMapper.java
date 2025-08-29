package com.backend.demo.component.mapper;

import com.backend.demo.component.dto.FacultyDto;
import com.backend.demo.repository.entity.Faculty;
import org.springframework.stereotype.Component;

@Component
public class FacultyMapper {
    public FacultyDto toDto(Faculty faculty) {
        return FacultyDto
                .builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .build();
    }
}
