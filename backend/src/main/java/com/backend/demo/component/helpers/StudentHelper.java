package com.backend.demo.component.helpers;

import ch.qos.logback.core.util.StringUtil;
import com.backend.demo.component.mapper.StudentMapper;
import com.backend.demo.component.request.StudentRegisterRequest;
import com.backend.demo.component.response.StudentResponse;
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
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StudentHelper {
    private final ModalityRepository modalityRepository;
    private final ProjectRepository projectRepository;
    private final DegreeRepository degreeRepository;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final MongoTemplate mongoTemplate;

    public StudentHelper(ModalityRepository modalityRepository, ProjectRepository projectRepository,
                         DegreeRepository degreeRepository, StudentRepository studentRepository, StudentMapper studentMapper,
                         MongoTemplate mongoTemplate) {
        this.modalityRepository = modalityRepository;
        this.projectRepository = projectRepository;
        this.degreeRepository = degreeRepository;
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Method to delete a student by their ID. We also delete the project and degree if he´s the only one related.
     * */
    @Transactional
    public void deleteStudent(String studentId) {
        Student student = studentRepository.findByStudentId(studentId).orElseThrow(()->
                new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "Not student was found with the ID " + studentId));
        Degree degree = degreeRepository.findById(student.getDegreeId()).orElseThrow(()->
                new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "Not degree was found"));
        Project project = projectRepository.findById(degree.getProject()).orElseThrow(()->
                new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "Not project was found"));

        if (studentRepository.countStudentsByDegreeId(degree.getId()) <= 1) {
            if (degreeRepository.countDegreesByProject(degree.getId()) <= 1) {
                projectRepository.delete(project);
            }
            degreeRepository.delete(degree);
        }

        studentRepository.delete(student);
    }

    /**
     * Method to get the student data filtered for the table
     * */
    public List<StudentResponse> getFilteredData(String facultyId, String careerId, String generation, String modalityId) {
        /*
         * Using CriteriaAPI for complex queries.
         */
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
        List<Student> res = mongoTemplate.aggregate(agg, "Student", Student.class).getMappedResults();

        return res.stream().map(studentMapper::toDto).toList();
    }


    /**
     * Method to get a dynamic map with data based on the inputs.
     * CUSTOM MONGODB QUERY.
     * */
    public Map<String, Integer> getChartData(String facultyId, String careerId, String generation, String modalityId) {
        Criteria criteria = new Criteria();
        // List for the aggregations
        List<AggregationOperation> ops = new ArrayList<>();

        // Establecemos la agregacion  y group por defecto en caso de que no se envíen datos (facultad)
        GroupOperation group = Aggregation.group("faculty.name").count().as("total");
        ops = getAggregations("Faculty", "facultyId", "_id", "faculty");

        // Start the dynamic query based on the data provided
        if (!StringUtil.isNullOrEmpty(facultyId)) {
            ops = getAggregations("Career", "careerId", "_id", "career");
            criteria = criteria.and("facultyId").is(facultyId);
            group = Aggregation.group("career.name").count().as("total");
        }
        if (!StringUtil.isNullOrEmpty(careerId)) {
            criteria = criteria.and("careerId").is(careerId);
            group = Aggregation.group("generation").count().as("total");
        }
        if (!StringUtil.isNullOrEmpty(generation) || !StringUtil.isNullOrEmpty(modalityId)) {
            if (!StringUtil.isNullOrEmpty(generation)) {
                criteria = criteria.and("generation").is(generation);
            }
            if (!StringUtil.isNullOrEmpty(modalityId)) {
                criteria = criteria.and("modality").is(modalityId);
            }

            // We´ll show the modality name and count by that
            ops = getAggregations("Degree", "degreeId", "_id", "degree");
            ops.addAll(getAggregations("Modality", "degree.modality", "_id", "modality"));
            group = Aggregation.group("modality.name").count().as("total");
        }

        ops.add(Aggregation.match(criteria));

        // Add the group
        ops.add(group);

        // Crear la agregación y ejecutarla para obtener los resultados
        Aggregation agg = Aggregation.newAggregation(ops);
        List<Document> doc = mongoTemplate.aggregate(agg, "Student", Document.class).getMappedResults();

        // We return a map with the results of the dynamic query executed.
        return doc.stream().collect(Collectors.toMap(
                d -> d.getString("_id"),
                d -> d.getInteger("total")
        ));
    }

    /**
     * HELPER Method: Gets the unwind aggregation for our database.
     * */
    public List<AggregationOperation> getAggregations(String from, String localField, String foreignField, String as) {
        List<AggregationOperation> res = new ArrayList<>();
        res.add(Aggregation.addFields()
                .addField(as + "ObjId")
                .withValue(ConvertOperators.ToObjectId.toObjectId("$" + localField))
                .build()
        );

        res.add(Aggregation.lookup(from, as + "ObjId", foreignField, as));
        res.add(Aggregation.unwind(as));

        return res;
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
