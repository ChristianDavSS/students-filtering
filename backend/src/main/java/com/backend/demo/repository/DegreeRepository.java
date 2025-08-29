package com.backend.demo.repository;

import com.backend.demo.repository.entity.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DegreeRepository extends JpaRepository<Degree, Long>, JpaSpecificationExecutor<Degree> {
}
