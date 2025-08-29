package com.backend.demo.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DegreeTeacher {
    /*
    * This will be the ID of the whole data (composite PK) but it´s decomposed into the three relationships.
    * Should be the representative PK for the repository
     */
    @EmbeddedId
    private DegreeTeacherPK degreeTeacherId;

    @ManyToOne
    @MapsId("degree_id")
    @JoinColumn(name = "degree_id", nullable = false)
    private Degree degree;

    @ManyToOne
    @MapsId("teacher_id")
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @MapsId("role_id")
    @JoinColumn(name = "role_id", nullable = false)
    private Roles roles;

}
