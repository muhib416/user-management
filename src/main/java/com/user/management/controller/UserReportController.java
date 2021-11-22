package com.user.management.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.user.management.service.UserService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.FileInputStream;
import java.util.HashMap;

@Controller
@RequestMapping(path = "/api/userReport")
public class UserReportController {

    Logger logger = LoggerFactory.getLogger(UserReportController.class);

    @Autowired
    UserService userService;
    
    @GetMapping(path = "/pdf")
    public ResponseEntity<byte[]> generatePdf() throws Exception, JRException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(userService.getUsers());
        JasperReport compileReport =  JasperCompileManager.compileReport(new FileInputStream("src/main/resources/invoice.jrxml"));
        
        HashMap<String, Object> map = new HashMap<>();
        JasperPrint report =  JasperFillManager.fillReport(compileReport, map, beanCollectionDataSource);
        //JasperExportManager.exportReportToPdfFile(report, "invoice.pdf");
        byte[] reportPdfFile = JasperExportManager.exportReportToPdf(report);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=invoice.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(reportPdfFile);
    }
}
