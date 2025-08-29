package com.backend.demo.repository;

import com.backend.demo.repository.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<Roles, Long>, JpaSpecificationExecutor<Roles> {
}
