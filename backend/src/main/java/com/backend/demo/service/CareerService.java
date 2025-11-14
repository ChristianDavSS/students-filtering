package com.backend.demo.service;

import com.backend.demo.repository.CareerRepository;
import org.springframework.stereotype.Service;

@Service
public class CareerService {
    private final CareerRepository careerRepository;

    public CareerService(CareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }
}
