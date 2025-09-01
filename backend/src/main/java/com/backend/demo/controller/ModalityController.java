package com.backend.demo.controller;

import com.backend.demo.component.dto.ModalityDto;
import com.backend.demo.component.mapper.ModalityMapper;
import com.backend.demo.service.ModalityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/modality")
public class ModalityController {
    private final ModalityService modalityService;
    private final ModalityMapper modalityMapper;
    public ModalityController(ModalityService modalityService, ModalityMapper modalityMapper) {
        this.modalityService = modalityService;
        this.modalityMapper = modalityMapper;
    }

    @GetMapping
    public List<ModalityDto> getAllModalities() {
        return modalityService.getAllModalities();
    }
}
