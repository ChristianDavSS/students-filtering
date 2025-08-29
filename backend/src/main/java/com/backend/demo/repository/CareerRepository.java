package com.backend.demo.repository;

import com.backend.demo.repository.entity.Career;
import com.backend.demo.repository.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CareerRepository extends JpaRepository<Career, Long>, JpaSpecificationExecutor<Career> {
    List<Career> getCareersByFaculty(Faculty faculty);
}
