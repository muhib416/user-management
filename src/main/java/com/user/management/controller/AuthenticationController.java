package com.user.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.user.management.model.Student;
import com.user.management.repository.StudentRepository;
import com.user.management.service.UserService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/authentication")
public class AuthenticationController {

    Integer grade = 1;
    public static String staticKey = "Eng000";
    @Autowired
    StudentRepository studentRepository;
    
    @Autowired
    UserService userService;

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
    
    @GetMapping(path = "/pdf")
    public ResponseEntity<byte[]> generatePdf() throws Exception, JRException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(userService.getUsers());
        JasperReport compileReport =  JasperCompileManager.compileReport(new FileInputStream("src/main/resources/invoice.jrxml"));
        
        HashMap<String, Object> map = new HashMap<>();
        JasperPrint report =  JasperFillManager.fillReport(compileReport, map, beanCollectionDataSource);
        //JasperExportManager.exportReportToPdfFile(report, "invoice.pdf");
        //http://localhost:9191/authentication/pdf
        byte[] reportPdfFile = JasperExportManager.exportReportToPdf(report);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=invoice.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(reportPdfFile);
    }
}
