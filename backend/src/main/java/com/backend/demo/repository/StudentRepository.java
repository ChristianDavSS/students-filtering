package com.backend.demo.repository;

import com.backend.demo.repository.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String>, JpaSpecificationExecutor<Student> {
}
