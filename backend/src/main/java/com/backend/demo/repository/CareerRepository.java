package com.backend.demo.repository;

import com.backend.demo.repository.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CareerRepository extends JpaRepository<Career, Long>, JpaSpecificationExecutor<Career> {
}
