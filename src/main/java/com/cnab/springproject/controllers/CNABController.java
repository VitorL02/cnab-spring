package com.cnab.springproject.controllers;

import com.cnab.springproject.services.CNABService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private static final String CNAB = "CNAB - Processamento";
    private static final String CNAB_RESUMO = "Esse endpoint permite o upload de um CNAB.txt";
    private static final String CNAB_DESCRICAO = "Esse endpoint permite o upload de um CNAB em formato .txt para processamento assincrono dos dados do CNAB";
    @Autowired
    private CNABService cnabService;

    @PostMapping(name = "/upload",consumes = {"multipart/form-data"})
    @Tag(name=CNAB)
    @Operation(summary = CNAB_RESUMO,description = CNAB_DESCRICAO)
    public ResponseEntity upload(@RequestParam (value = "file") MultipartFile file) throws Exception {

        cnabService.uploadCnabFile(file);
        return ResponseEntity.status(HttpStatus.OK).body("Processamento iniciado. Aguarde!");
    }

}
