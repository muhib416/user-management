package com.user.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.user.management.model.Student;
import com.user.management.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/authentication")
public class AuthenticationController {

    Integer grade = 1;
    public static String staticKey = "Eng000";
    @Autowired
    StudentRepository studentRepository;

    @GetMapping(path = "/student/insert_data")
    public String insertDatas() {
        Student student = new Student(
                staticKey + grade, "John Doe " + grade, Student.Gender.MALE, grade);
        studentRepository.save(student);
        grade++;
        return student.toString();
    }

    @GetMapping(path = "/student/get_all_data")
    public String getAllDatas() {
        List<Student> students = new ArrayList<>();
        studentRepository.findAll().forEach(students::add);
        return students.toString();
    }

    @GetMapping(path = "/student/get_data_by_key")
    public String getAllDatas(@RequestParam(value = "id") String id) {
        System.out.println(staticKey + id);
        Student retrievedStudent = studentRepository.findById(staticKey + id).get();
        return retrievedStudent.toString();
    }
}
