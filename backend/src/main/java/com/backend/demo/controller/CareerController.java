package com.backend.demo.controller;

import com.backend.demo.component.dto.CareerDto;
import com.backend.demo.service.CareerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/career")
public class CareerController {
    // Dependency injection
    private final CareerService careerService;
    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }

    @GetMapping
    public List<CareerDto> getAllCareers(@RequestParam(required = false) Long facultyId) {
        return careerService.getAllCareers(facultyId);
    }

}
