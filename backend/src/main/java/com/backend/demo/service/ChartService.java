package com.backend.demo.service;

import ch.qos.logback.core.util.StringUtil;
import com.backend.demo.repository.CareerRepository;
import com.backend.demo.repository.FacultyRepository;
import com.backend.demo.repository.ModalityRepository;
import com.backend.demo.repository.StudentRepository;
import com.backend.demo.repository.entity.Career;
import com.backend.demo.repository.entity.Faculty;
import com.backend.demo.repository.entity.Modality;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChartService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final CareerRepository careerRepository;
    private final ModalityRepository modalityRepository;

    public ChartService(StudentRepository studentRepository, FacultyRepository facultyRepository,
                        CareerRepository careerRepository, ModalityRepository modalityRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.careerRepository = careerRepository;
        this.modalityRepository = modalityRepository;
    }

    public Map<String, Long> getData(Long facultyId, Long careerId, String generation, Long modalityId) {
        Map<String, Long> map = new HashMap<>();
        List<Tuple> values = facultyRepository.countStudentsByFaculty();
        Faculty fac = null;
        Career car = null;

        if (facultyId != null) {
            fac = facultyRepository.findById(facultyId).orElse(null);
            values = careerRepository.countStudentsByCareer(fac);
        }
        if (careerId != null) {
            car = careerRepository.findById(careerId).orElse(null);
            values = studentRepository.countStudentsByGeneration(fac, car);
        }
        if (!StringUtil.isNullOrEmpty(generation)) {
            values = modalityRepository.countStudentsByModality(fac, car, generation);
        }
        if (modalityId != null) {
            Modality mod = modalityRepository.findById(modalityId).orElse(null);
            values = modalityRepository.countStudentsByAll(fac, car, generation, mod);
        }

        for (Tuple value : values) {
            map.put(value.get("name", String.class), value.get("quantity", Long.class));
        }

        return map;
    }
}
