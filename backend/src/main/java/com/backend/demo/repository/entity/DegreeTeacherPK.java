package com.backend.demo.repository.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
/**
 * @class DegreeTeacherPK: Embeddable class to create a composite primary key with 3 values (id´s)
 */
public class DegreeTeacherPK implements Serializable {
    private Long degree_id;
    private Long teacher_id;
    private Long role_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DegreeTeacherPK that = (DegreeTeacherPK) o;
        return Objects.equals(degree_id, that.degree_id) && Objects.equals(teacher_id, that.teacher_id) && Objects.equals(role_id, that.role_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(degree_id, teacher_id, role_id);
    }
}
