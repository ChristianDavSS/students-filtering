package com.backend.demo.component.mapper;

import com.backend.demo.component.helpers.NameInterface;
import com.backend.demo.component.response.NameResponse;
import org.springframework.stereotype.Component;

@Component
public class NameMapper {
    public NameResponse toDto(NameInterface obj) {
        return NameResponse.builder()
                .id(obj.getId())
                .name(obj.getName())
                .build();
    }

    public NameResponse toDto(String generation) {
        return NameResponse.builder()
                .id(null)
                .name(generation)
                .build();
    }
}
