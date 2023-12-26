package com.cnab.springproject.controllers;

import com.cnab.springproject.services.CNABService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/cnab")
public class CNABController {

    @Autowired
    private CNABService cnabService;

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam ("file") MultipartFile file) throws Exception {

        cnabService.uploadCnabFile(file);
        return ResponseEntity.status(HttpStatus.OK).body("Processamento iniciado. Aguarde!");
    }

}
