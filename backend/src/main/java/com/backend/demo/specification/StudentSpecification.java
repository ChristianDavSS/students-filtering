package com.backend.demo.specification;

import ch.qos.logback.core.util.StringUtil;
import com.backend.demo.repository.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StudentSpecification {
    // EntityManager: Used to interact with the database for custom queries (persistance context)
    private final EntityManager entityManager;
    public StudentSpecification(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
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

    /**
     * Method to filter students by data and return a custom map depending on the inputs
     * On this method, we create a dynamic SQL query with the input we got. Depending on the
     * inputs there´ll be a different key-value map to create a chart on the frontend.
     * BACKEND OPTIMIZATION :D
     * */
    public Map<String, Long> filterBy(Long facultyId, Long careerId, String generation, Long modalityId) {
        // Define the query, root, criteriaBuilder & the predicates by the entityManager
        // Criteria Builder: A WHERE clause.
        CriteriaBuilder cB = entityManager.getCriteriaBuilder();
        // Criteria Query: The query we are building
        CriteriaQuery<Object[]> query = cB.createQuery(Object[].class);
        // Root: The FROM clause
        Root<Student> root = query.from(Student.class);
        // Predicate: List of booleans to filter data. Java Util Class
        List<Predicate> predicates = new ArrayList<>();

        // Definition of the path (SELECT & GROUP BY)
        Path<?> p = null;
        // Start to create the dynamic query with the path. (looking on the inputs)
        // There are the GROUP BY and SELECT, so I´ll just need one to be executed.
        if (!StringUtil.isNullOrEmpty(generation) || modalityId != null) {
            p = root.get("degree").get("modality").get("name");
        } else if (careerId != null) {
            p = root.get("generation");
        } else if (facultyId != null){
            p = root.get("career").get("name");
        } else {
            p = root.get("faculty").get("name");
        }

        // We select and group by the results by the path.
        query.multiselect(p, cB.count(root));
        query.groupBy(p);

        // Set the WHERE clauses. As there could be multiple filters, we put multiple if´s statements
        if (facultyId != null) predicates.add(cB.equal(root.get("faculty").get("id"), facultyId));
        if (careerId != null) predicates.add(cB.equal(root.get("career").get("id"), careerId));
        if (!StringUtil.isNullOrEmpty(generation)) predicates.add(cB.equal(root.get("generation"), generation));
        if (modalityId != null) predicates.add(cB.equal(root.get("degree").get("modality").get("id"), modalityId));

        // We only create a WHERE if we got filters.
        if (!predicates.isEmpty()) query.where(cB.and(predicates.toArray(new Predicate[0])));

        // Get the results and returning a map with them
        List<Object[]> results = entityManager.createQuery(query).getResultList();

        /*
        * NOTICE: Query has all the SQL statements as methods, so I built up the query from there.
        * */

        // We turn the results into a map (Java lambdas)
        return results.stream().collect(Collectors.toMap(
                result -> (String) result[0],
                result -> (Long) result[1]
        ));
    }
}
