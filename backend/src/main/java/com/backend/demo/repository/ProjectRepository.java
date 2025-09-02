package com.backend.demo.repository;

import com.backend.demo.repository.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    @Query("SELECT p FROM Project p WHERE LOWER(TRIM(p.name)) = LOWER(TRIM(:name))")
    Optional<Project> findByName(@Param("name") String name);
}
