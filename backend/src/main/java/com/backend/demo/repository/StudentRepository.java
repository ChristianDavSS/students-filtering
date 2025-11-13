package com.backend.demo.repository;

import com.backend.demo.repository.entity.*;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String>, JpaSpecificationExecutor<Student> {
    @Query("SELECT COUNT(st.id) FROM Student st JOIN st.degree deg WHERE deg = :degree")
    Long countStudentsByDegree(@Param("degree") Degree degree);

    @Query("SELECT COUNT(st.id) FROM Student st JOIN st.degree deg JOIN deg.project p WHERE p = :project")
    Long countStudentsByProject(@Param("project") Project project);
}
