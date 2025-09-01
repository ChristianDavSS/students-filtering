package com.backend.demo.component.mapper;

import com.backend.demo.component.dto.ModalityDto;
import com.backend.demo.repository.entity.Modality;
import org.springframework.stereotype.Component;

@Component
public class ModalityMapper {
    public ModalityDto toDto(Modality modality) {
        return ModalityDto
                .builder()
                .id(modality.getId())
                .name(modality.getName())
                .build();
    }
}
