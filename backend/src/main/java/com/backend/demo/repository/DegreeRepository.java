package com.backend.demo.repository;

import com.backend.demo.repository.entity.Degree;
import com.backend.demo.repository.entity.Modality;
import com.backend.demo.repository.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface DegreeRepository extends JpaRepository<Degree, Long>, JpaSpecificationExecutor<Degree> {
    @Query("SELECT deg FROM Degree deg WHERE deg.date = :date AND deg.modality = :modality AND deg.project = :project")
    Optional<Degree> findByAllData(@Param("date")Date date, @Param("modality")Modality modality,
                                   @Param("project")Project project);
}
