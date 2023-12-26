package com.cnab.springproject.controllers.reports;

import com.cnab.springproject.entidades.TransacaoReport;
import com.cnab.springproject.services.reports.TransacaoReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("report")
public class TransacaoReportController {

    @Autowired
    private TransacaoReportService transacaoReportService;

    @GetMapping
    List<TransacaoReport> listAll(){
        List<TransacaoReport> transacaoReports = transacaoReportService.listTransactionsNameStore();
        return transacaoReports;
    }
}
