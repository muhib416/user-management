package com.user.management.controller;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.user.management.dto.*;
import com.user.management.service.UserService;
import com.user.management.util.BaseHeader;
import com.user.management.util.Utils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<?> getUserByID(@RequestHeader Map<String, String> headers, @PathVariable(name = "id") Long id) {
        try {
            BaseHeader baseHeader = Utils.getHeaderValue(headers);
            //check if user eligible to edit
            if (!Utils.isUserEligibleToEdit(baseHeader.getToken(), id.intValue())) {
                return ResponseEntity.badRequest().body(new ParentResponse(new CommonResponse("User not eligible to see other profile", "NOT_OK", "")));
            }

            UserDto user = userService.getUserByID(id);
            if (null == user) {
                return ResponseEntity.badRequest().body(new ParentResponse(new CommonResponse("User not found for this id :: " + id, "NOT_OK", "")));
            }

            LoginResponse loginResponse = new LoginResponse("", "OK", "Success get user", user, new JwtResponse());
            return ResponseEntity.ok().body(new ParentResponse(loginResponse));
        } catch (Exception e) {
            logger.debug("Caught error : " + e.getMessage());
            return ResponseEntity.badRequest().body(new ParentResponse(new CommonResponse("Maaf terjadi gangguan", "NOT_OK", "")));
        }
    }

    @GetMapping(path = "/all")
    public List<UserDto> getAllUser() {
        return userService.getUsers();
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        try {
            UserDto user = userService.addUser(userDto);
            LoginResponse loginResponse = new LoginResponse("", "OK", "Success add user!", user, new JwtResponse());
            return ResponseEntity.ok().body(new ParentResponse(loginResponse));
        } catch (Exception e) {
            logger.debug("Caught error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @PostMapping(path = "/edit/{id}")
    public ResponseEntity<?> editUserByID(@RequestHeader Map<String, String> headers, @PathVariable(name = "id") Long id,
                                          @RequestBody UserDto userDto) {
        try {
            BaseHeader baseHeader = Utils.getHeaderValue(headers);
            //check if user eligible to edit
            if (!Utils.isUserEligibleToEdit(baseHeader.getToken(), id.intValue())) {
                return ResponseEntity.badRequest().body(new ParentResponse(new CommonResponse("User not eligible to edit other profile", "NOT_OK", "")));
            }

            UserDto user = userService.editUserByID(id, userDto);
            if (null == user) {
                return ResponseEntity.badRequest().body(new ParentResponse(new CommonResponse("", "NOT_OK", "User not found for this id :: " + id)));
            }

            LoginResponse loginResponse = new LoginResponse("", "OK", "Success edit profile!", user, new JwtResponse());
            return ResponseEntity.ok().body(new ParentResponse(loginResponse));
        } catch (Exception e) {
            logger.debug("Caught error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @PostMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteUserByID(@RequestHeader Map<String, String> headers, @PathVariable(name = "id") Long id) {
        try {
            BaseHeader baseHeader = Utils.getHeaderValue(headers);

            //check if user eligible to edit
            if (!Utils.isUserEligibleToEdit(baseHeader.getToken(), id.intValue())) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("User not eligible to delete other profile", "NOT_OK", "")));
            }

            UserDto user = userService.deleteUserByID(id);
            if (null == user) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("User not found for this id :: " + id, "NOT_OK", "")));
            }

            LoginResponse loginResponse = new LoginResponse("", "OK", "Success delete user!", user, new JwtResponse());
            ParentResponse parentResponse = new ParentResponse(loginResponse);
            return ResponseEntity.ok().body(parentResponse);
        } catch (Exception e) {
            logger.debug("Caught error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }
    
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
