package com.backend.demo.service;

import com.backend.demo.component.dto.NameDto;
import com.backend.demo.component.mapper.NameMapper;
import com.backend.demo.repository.ModalityRepository;
import com.backend.demo.repository.entity.Modality;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModalityService {
    private final ModalityRepository modalityRepository;
    private final NameMapper nameMapper;

    public ModalityService(ModalityRepository modalityRepository, NameMapper nameMapper) {
        this.modalityRepository = modalityRepository;
        this.nameMapper = nameMapper;
    }

    public List<NameDto> getAllModalities() {
        List<Modality> modalities = modalityRepository.findAll();
        return modalities.stream().map(nameMapper::toDto).toList();
    }
}
