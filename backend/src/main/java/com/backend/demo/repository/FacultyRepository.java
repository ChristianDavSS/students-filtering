package com.backend.demo.repository;

import com.backend.demo.repository.entity.Faculty;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long>, JpaSpecificationExecutor<Faculty> {
    Optional<Faculty> findByName(String faculty);

    @Query("SELECT f.name AS name, COUNT(st.id) AS quantity" +
            " FROM Student st JOIN st.faculty f GROUP BY f.name")
    List<Tuple> countStudentsByFaculty();
}
