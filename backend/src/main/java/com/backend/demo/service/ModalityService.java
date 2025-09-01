package com.backend.demo.service;

import com.backend.demo.component.dto.ModalityDto;
import com.backend.demo.component.mapper.ModalityMapper;
import com.backend.demo.repository.ModalityRepository;
import com.backend.demo.repository.entity.Modality;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModalityService {
    private final ModalityRepository modalityRepository;
    private final ModalityMapper modalityMapper;

    public ModalityService(ModalityRepository modalityRepository, ModalityMapper modalityMapper) {
        this.modalityRepository = modalityRepository;
        this.modalityMapper = modalityMapper;
    }

    public List<ModalityDto> getAllModalities() {
        List<Modality> modalities = modalityRepository.findAll();
        return modalities.stream().map(modalityMapper::toDto).toList();
    }
}
