package com.backend.demo.repository;

import com.backend.demo.repository.entity.*;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String> {
}
