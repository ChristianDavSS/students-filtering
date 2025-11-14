package com.backend.demo.component.helpers;

import ch.qos.logback.core.util.StringUtil;
import com.backend.demo.component.request.StudentRegisterRequest;
import com.backend.demo.repository.DegreeRepository;
import com.backend.demo.repository.ModalityRepository;
import com.backend.demo.repository.ProjectRepository;
import com.backend.demo.repository.StudentRepository;
import com.backend.demo.repository.entity.Degree;
import com.backend.demo.repository.entity.Modality;
import com.backend.demo.repository.entity.Project;
import com.backend.demo.repository.entity.Student;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class StudentHelper {
    private final ModalityRepository modalityRepository;
    private final ProjectRepository projectRepository;
    private final DegreeRepository degreeRepository;
    private final StudentRepository studentRepository;
    private final MongoTemplate mongoTemplate;

    public StudentHelper(ModalityRepository modalityRepository, ProjectRepository projectRepository,
                         DegreeRepository degreeRepository, StudentRepository studentRepository, MongoTemplate mongoTemplate) {
        this.modalityRepository = modalityRepository;
        this.projectRepository = projectRepository;
        this.degreeRepository = degreeRepository;
        this.studentRepository = studentRepository;
        this.mongoTemplate = mongoTemplate;
    }


    /**
     * Method to get a dynamic map with data based on the inputs.
     * CUSTOM MONGODB QUERY.
     * */
    public Map<String, Long> getChartData(String facultyId, String careerId, String generation, String modalityId) {
        // Using CriteriaAPI for complex queries.
        Query query = new Query();
        // MATCH: Create the filters
        Criteria criteria = new Criteria();
        // List for the aggregations
        List<AggregationOperation> ops = new ArrayList<>();

        // Cast the degreeId from string to ObjectId so the lookup will work on it.
        // Lookup only works with field that are ObjectId
        ops.add(Aggregation.addFields()
                .addField("degreeObjId")
                .withValue(ConvertOperators.ToObjectId.toObjectId("$degreeId"))
                .build());
        // Create the lookup with the new field as the localField
        ops.add(Aggregation.lookup("Degree", "degreeObjId", "_id", "degree"));
        ops.add(Aggregation.unwind("degree"));

        // Flag to control the criteria
        boolean flag = false;

        if (!StringUtil.isNullOrEmpty(facultyId)) {
            criteria = criteria.and("facultyId").is(facultyId);
            flag = true;
        }
        if (!StringUtil.isNullOrEmpty(careerId)) {
            criteria = criteria.and("careerId").is(careerId);
            flag = true;
        }
        if (!StringUtil.isNullOrEmpty(generation)) {
            criteria = criteria.and("generation").is(generation);
            flag = true;
        }
        if (!StringUtil.isNullOrEmpty(modalityId)) {
            criteria = criteria.and("modality").is(modalityId);
            flag = true;
        }

        // If there were a criteria we match it
        if (flag) {
            ops.add(Aggregation.match(criteria));
        } else {
            ops.add(Aggregation.match(new Criteria()));
        }

        // Execute the aggregation
        Aggregation agg = Aggregation.newAggregation(ops);

        // Execute the whole query
        AggregationResults<Student> res = mongoTemplate.aggregate(agg, "Student", Student.class);
        System.out.println(res.getMappedResults());

        return null;
    }


    /**
     * Method to register a student into the database with all his data.
     * */
    @Transactional
    public Student registerStudent(StudentRegisterRequest request) {
        // If there´s already a student with their ID we throw an error.
        if (studentRepository.existsByStudentId(request.getStudentId())) {
            throw new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "Student already registered");
        }

        // Obtain the modality and the project by their id and name.
        Modality modality = modalityRepository.findById(request.getModalityId()).orElseThrow(()->
                new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "Error en los datos"));
        Project project = projectRepository.findByName(request.getProject()).orElseGet(()->
                projectRepository.save(Project.builder()
                        .name(request.getProject())
                        .build()
                ));

        // We look if there´s any other degree with the exact same data
        Degree degree = degreeRepository.findDegreeByDateAndModalityAndProjectAndTeachers(
                request.getDate(), modality.getId(), project.getId(), request.getTeachers()
        ).orElseGet(()->
                degreeRepository.save(Degree.builder()
                        .date(request.getDate())
                        .modality(modality.getId())
                        .project(project.getId())
                        .teachers(request.getTeachers())
                        .build()
                ));

        // Return the student created
        return studentRepository.save(
                Student.builder()
                        .studentId(request.getStudentId())
                        .name(request.getName())
                        .generation(request.getGeneration())
                        .facultyId(request.getFacultyId())
                        .careerId(request.getCareerId())
                        .degreeId(degree.getId())
                        .build()
        );
    }
}
