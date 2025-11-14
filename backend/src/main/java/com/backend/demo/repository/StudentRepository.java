package com.backend.demo.repository;

import com.backend.demo.repository.entity.*;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    Boolean existsByStudentId(String studentId);
    Optional<Student> findByStudentId(String studentId);
    Long countStudentsByDegreeId(String degreeId);
}
