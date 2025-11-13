package com.backend.demo.repository;

import com.backend.demo.repository.entity.Career;
import com.backend.demo.repository.entity.Faculty;
import com.backend.demo.repository.entity.Modality;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModalityRepository extends JpaRepository<Modality, Long>, JpaSpecificationExecutor<Modality> {
}
