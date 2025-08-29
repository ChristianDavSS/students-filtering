package com.backend.demo.repository;

import com.backend.demo.repository.entity.DegreeTeacher;
import com.backend.demo.repository.entity.DegreeTeacherPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DegreeTeacherRepository extends JpaRepository<DegreeTeacher, DegreeTeacherPK>,
        JpaSpecificationExecutor<DegreeTeacher> {
}
