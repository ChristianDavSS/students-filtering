package com.backend.demo.repository;

import com.backend.demo.repository.entity.Career;
import com.backend.demo.repository.entity.Faculty;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CareerRepository extends JpaRepository<Career, Long>, JpaSpecificationExecutor<Career> {
    List<Career> getCareersByFaculty(Faculty faculty);

    @Query("SELECT c.name AS name, COUNT(st.id) AS quantity FROM Student st JOIN st.career c" +
            " WHERE (:faculty IS NULL OR c.faculty = :faculty) GROUP BY c.name")
    List<Tuple> countStudentsByCareer(@Param("faculty") Faculty faculty);
}
