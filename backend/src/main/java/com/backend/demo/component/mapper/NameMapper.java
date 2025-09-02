package com.backend.demo.component.mapper;

import com.backend.demo.component.dto.NameDto;
import com.backend.demo.repository.entity.*;
import org.springframework.stereotype.Component;

@Component
public class NameMapper {
    public NameDto toDto(Career career) {
        return NameDto
                .builder()
                .id(career.getId())
                .name(career.getName())
                .build();
    }

    public NameDto toDto(Faculty faculty) {
        return NameDto
                .builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .build();
    }

    public NameDto toDto(Modality modality) {
        return NameDto
                .builder()
                .id(modality.getId())
                .name(modality.getName())
                .build();
    }

    public NameDto toDto(Teacher teacher) {
        return NameDto
                .builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .build();
    }
}
