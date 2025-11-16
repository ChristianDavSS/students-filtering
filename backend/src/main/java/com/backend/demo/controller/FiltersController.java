package com.backend.demo.controller;

import com.backend.demo.component.response.NameResponse;
import com.backend.demo.service.FiltersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/filters")
public class FiltersController {
    private final FiltersService filtersService;

    public FiltersController(FiltersService filtersService) {
        this.filtersService = filtersService;
    }

    @GetMapping
    public Map<String, List<NameResponse>> getFiltersData() {
        return filtersService.getFiltersData();
    }
}
