package com.backend.demo.repository;

import com.backend.demo.repository.entity.Modality;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ModalityRepository extends MongoRepository<Modality, String> {
}
