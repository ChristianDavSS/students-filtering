package com.backend.demo.repository;

import com.backend.demo.repository.entity.Modality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ModalityRepository extends JpaRepository<Modality, Long>, JpaSpecificationExecutor<Modality> {
}
