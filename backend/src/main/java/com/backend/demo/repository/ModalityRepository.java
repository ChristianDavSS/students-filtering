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
    @Query("SELECT m.name AS name, COUNT(st.id) AS quantity FROM Student st" +
            " JOIN st.degree dg JOIN dg.modality m" +
            " WHERE (:faculty IS NULL OR st.faculty = :faculty) AND" +
            " (:career IS NULL OR st.career = :career) AND" +
            " (:generation IS NULL OR st.generation = :generation)" +
            " GROUP BY m.name")
    List<Tuple> countStudentsByModality(@Param("faculty") Faculty faculty, @Param("career") Career career,
                                        @Param("generation") String generation);

    @Query("SELECT m.name AS name, COUNT(st.id) AS quantity FROM Student st" +
            " JOIN st.degree dg JOIN dg.modality m" +
            " WHERE (:faculty IS NULL OR st.faculty = :faculty) AND" +
            " (:career IS NULL OR st.career = :career) AND" +
            " (:generation IS NULL OR st.generation = :generation) AND" +
            " (:modality IS NULL OR dg.modality = :modality)" +
            " GROUP BY m.name")
    List<Tuple> countStudentsByAll(@Param("faculty") Faculty faculty, @Param("career") Career career,
                                   @Param("generation") String generation, @Param("modality") Modality modality);
}
