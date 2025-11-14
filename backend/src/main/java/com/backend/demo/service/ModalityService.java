package com.backend.demo.service;

import com.backend.demo.repository.ModalityRepository;
import org.springframework.stereotype.Service;

@Service
public class ModalityService {
    private final ModalityRepository modalityRepository;

    public ModalityService(ModalityRepository modalityRepository) {
        this.modalityRepository = modalityRepository;
    }
}
