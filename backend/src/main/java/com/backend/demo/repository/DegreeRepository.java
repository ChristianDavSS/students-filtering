package com.backend.demo.repository;

import com.backend.demo.repository.entity.Degree;
import com.backend.demo.repository.enums.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

public interface DegreeRepository extends MongoRepository<Degree, String> {
    Optional<Degree> findDegreeByDateAndModalityAndProjectAndTeachers(Date date, String modality, String project,
                                                                      Map<Role, String> teachers);
    Long countDegreesByProject(String projectId);
}
