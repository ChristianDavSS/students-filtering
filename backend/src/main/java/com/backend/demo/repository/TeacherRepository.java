package com.backend.demo.repository;

import com.backend.demo.repository.entity.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeacherRepository extends MongoRepository<Teacher, String> {
}
