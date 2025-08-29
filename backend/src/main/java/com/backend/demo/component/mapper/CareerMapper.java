package com.backend.demo.component.mapper;

import com.backend.demo.component.dto.CareerDto;
import com.backend.demo.repository.entity.Career;
import org.springframework.stereotype.Component;

@Component
public class CareerMapper {
    public CareerDto toDto(Career career) {
        return CareerDto
                .builder()
                .id(career.getId())
                .name(career.getName())
                .build();
    }
}
