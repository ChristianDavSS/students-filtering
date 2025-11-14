package com.backend.demo.repository;

import com.backend.demo.repository.entity.Faculty;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FacultyRepository extends MongoRepository<Faculty, String> {
}
