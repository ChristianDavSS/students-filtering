package com.backend.demo.repository;

import com.backend.demo.repository.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, Long>, JpaSpecificationExecutor<Roles> {
    @Query("SELECT role from Roles role WHERE LOWER(TRIM(REPLACE(role.name, ' ', ''))) = LOWER(TRIM(REPLACE(:name, ' ', '')))")
    Roles findByName(@Param("name") String name);
}
