package com.backend.demo.repository;

import com.backend.demo.repository.entity.Degree;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DegreeRepository extends MongoRepository<Degree, String> {
}
