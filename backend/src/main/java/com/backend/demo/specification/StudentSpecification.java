package com.backend.demo.specification;

import com.backend.demo.repository.entity.Student;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class StudentSpecification {
    /**
     * @param name: Name of the student to filter
     * @return Specification<Student>: The filtered query of the user matching a name
     */
    public Specification<Student> containsName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.trim(root.get("name"))),
                        "%" + name.trim().toLowerCase() + "%");
    }

    /**
     * @param id: ID of the student to filter. It has to be exact to have results.
     * @return Specification<Student>: The filtered query of the user matching an ID
     */
    public Specification<Student> hasId(String id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id);
    }

    /**
     * @param facultyId: String to see if there´s user with that exact faculty
     * @return Specification<Student>: The filtered query of the user matching a faculty
     */
    public Specification<Student> hasFacultyId(Long facultyId) {
        return (root, query, cB) ->
                cB.equal(root.get("faculty").get("id"), facultyId);
    }

    /**
     * @param careerId: String to see if there´s students related into that exact career
     * @return Specification<Student>: The filtered query of the user matching a career
     */
    public Specification<Student> hasCareerId(Long careerId) {
        return (root, query, cB) ->
                cB.equal(root.get("career").get("id"), careerId);
    }

    public Specification<Student> hasGeneration(String providedGeneration) {
        return (root, query, cB) ->
                cB.equal(cB.lower(cB.trim(root.get("generation"))), providedGeneration.trim().toLowerCase());
    }

    public Specification<Student> hasModalityId(Long modalityId) {
        return (root, query, cB) ->
                cB.equal(root.get("degree").get("modality").get("id"), modalityId);
    }
}
