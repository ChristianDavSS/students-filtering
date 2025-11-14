package com.backend.demo.repository;

import com.backend.demo.repository.entity.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProjectRepository extends MongoRepository<Project, String> {
    Optional<Project> findByName(String name);
}
