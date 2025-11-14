package com.backend.demo.repository;

import com.backend.demo.repository.entity.Career;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CareerRepository extends MongoRepository<Career, String> {
}
