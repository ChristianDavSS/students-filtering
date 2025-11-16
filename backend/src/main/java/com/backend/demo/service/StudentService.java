package com.backend.demo.service;

import com.backend.demo.component.helpers.StudentHelper;
import com.backend.demo.component.mapper.StudentMapper;
import com.backend.demo.component.request.StudentRegisterRequest;
import com.backend.demo.component.response.StudentResponse;
import com.backend.demo.repository.StudentRepository;
import com.backend.demo.repository.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final StudentHelper studentHelper;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper, StudentHelper studentHelper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.studentHelper = studentHelper;
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
    public List<StudentResponse> getFilteredData(String studentId, String name, String facultyId, String careerId,
                                                 String generation, String modalityId) {
        return studentHelper.getFilteredData(studentId, name, facultyId, careerId, generation, modalityId);
    }

    /**
     * Method to return data for the main frontend chart
     * */
    public Map<String, Integer> getChartData(String facultyId, String careerId, String generation, String modalityId) {
        return studentHelper.getChartData(facultyId, careerId, generation, modalityId);
    }

    /**
     * Method to remove a student from the database.
     * */
    public void deleteStudent(String studentId) {
        studentHelper.deleteStudent(studentId);
    }
}
