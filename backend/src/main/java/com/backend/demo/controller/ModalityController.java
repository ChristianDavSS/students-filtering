package com.backend.demo.controller;

import com.backend.demo.component.dto.NameDto;
import com.backend.demo.service.ModalityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/modality")
public class ModalityController {
    private final ModalityService modalityService;
    public ModalityController(ModalityService modalityService) {
        this.modalityService = modalityService;
    }

    @GetMapping
    public List<NameDto> getAllModalities() {
        return modalityService.getAllModalities();
    }
}
