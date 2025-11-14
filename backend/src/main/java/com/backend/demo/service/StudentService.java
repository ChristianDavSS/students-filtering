package com.backend.demo.service;

import ch.qos.logback.core.util.StringUtil;
import com.backend.demo.component.helpers.StudentHelper;
import com.backend.demo.component.mapper.StudentMapper;
import com.backend.demo.component.request.StudentRegisterRequest;
import com.backend.demo.component.response.StudentResponse;
import com.backend.demo.repository.StudentRepository;
import com.backend.demo.repository.entity.Student;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final StudentHelper studentHelper;
    private final MongoTemplate mongoTemplate;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper, StudentHelper studentHelper,
                          MongoTemplate mongoTemplate) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.studentHelper = studentHelper;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Method to register a new student. We ask for all the data of the student
     * */
    public Student registerStudent(StudentRegisterRequest request) {
        return studentHelper.registerStudent(request);
    }

    /**
     * Method to get all the students.
     * To Do: Make it pageable to improve the performance
     * */
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream().map(studentMapper::toDto).toList();
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
     * Method to return data for the main frontend chart
     * */
    public Map<String, Long> getChartData(String facultyId, String careerId, String generation, String modalityId) {
        return studentHelper.getChartData(facultyId, careerId, generation, modalityId);
    }

    /**
     * Method to remove a student from the database.
     * */
    public void deleteStudent(String studentId) {
        studentMapper.deleteStudent(studentId);
    }
}
