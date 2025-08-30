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
     * @param providedFaculty: String to see if there´s user with that exact faculty
     * @return Specification<Student>: The filtered query of the user matching a faculty
     */
    public Specification<Student> hasFaculty(String providedFaculty) {
        return (root, query, cB) ->
                cB.equal(cB.lower(cB.trim(root.get("faculty").get("name"))),
                        providedFaculty.trim().toLowerCase());
    }

    /**
     * @param providedCareer: String to see if there´s students related into that exact career
     * @return Specification<Student>: The filtered query of the user matching a career
     */
    public Specification<Student> hasCareer(String providedCareer) {
        return (root, query, cB) ->
                cB.equal(cB.lower(cB.trim(root.get("career").get("name"))),
                        providedCareer.trim().toLowerCase());
    }

    public Specification<Student> hasGeneration(String providedGeneration) {
        return (root, query, cB) ->
                cB.equal(cB.lower(cB.trim(root.get("generation"))), providedGeneration.trim().toLowerCase());
    }

    public Specification<Student> hasModality(String providedModality) {
        return (root, query, cB) ->
                cB.equal(cB.lower(cB.trim(root.get("degree").get("modality").get("name"))),
                        providedModality.trim().toLowerCase());
    }
}
