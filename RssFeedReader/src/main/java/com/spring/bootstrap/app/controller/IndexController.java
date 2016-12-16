/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.bootstrap.app.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author sandeep.s
 */
@Controller
@RequestMapping("/sample")
public class IndexController {

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity index(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String filePath = "C:\\Users\\sandeep.s\\Documents\\Sandeep_latest.docx";
        InputStream inputStream = new FileInputStream(new File(filePath));
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(Files.size(Paths.get(filePath)));
        headers.setContentDispositionFormData("attachment", "Sandeep_latest.docx");
        return new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK);
    }

}
