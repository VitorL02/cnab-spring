package com.cnab.springproject.controllers.reports;

import com.cnab.springproject.entidades.TransacaoReport;
import com.cnab.springproject.services.reports.TransacaoReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("report")
public class TransacaoReportController {

    private static final String REPORTS = "Relatorios - Processamento";
    private static final String REPORTS_RESUMO = "Esse endpoint fornece um relatorio de CNABS processados por loja";
    private static final String REPORTS_DESCRICAO = "Esse endpoint lista os relatorios de CNAB processados de acordo com o nome da loja, agrupando e informando total de saldo da loja juntamente com as transações realizadas";
    @Autowired
    private TransacaoReportService transacaoReportService;

    @GetMapping
    @Tag(name=REPORTS)
    @Operation(summary = REPORTS_RESUMO,description = REPORTS_DESCRICAO)
    List<TransacaoReport> listAll(){
        List<TransacaoReport> transacaoReports = transacaoReportService.listTransactionsNameStore();
        return transacaoReports;
    }
}
