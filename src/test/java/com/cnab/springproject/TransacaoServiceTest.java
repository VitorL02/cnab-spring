package com.cnab.springproject;

import com.cnab.springproject.entidades.TransacaoReport;
import com.cnab.springproject.entidades.TransacaoVO;
import com.cnab.springproject.repositories.TransactionReportRepository;
import com.cnab.springproject.services.reports.TransacaoReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import  org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

//Com sufixo Test representa teste de unidade
@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {
    @InjectMocks
    private TransacaoReportService transacaoReportService;
    @Mock
    private TransactionReportRepository transactionReportRepository;

    @Test
    public void testListTransacoesPorLoja(){
        final String lojaA = "Loja A", lojaB = "Loja B";
        var primeiraTransacao = new TransacaoVO(1L,1,new Date(System.currentTimeMillis()), BigDecimal.valueOf(180),3321321313L,"12343903213",
                new Time(System.currentTimeMillis()),"Dono da loja B", lojaB);
        var segundaTransacao = new TransacaoVO(1L,1,new Date(System.currentTimeMillis()), BigDecimal.valueOf(80),17045451024L,"12343903213",
                new Time(System.currentTimeMillis()),"Dono da loja A", lojaA);
        var terceiraTransacao = new TransacaoVO(1L,1,new Date(System.currentTimeMillis()), BigDecimal.valueOf(200),18613095038L,"12343903213",
                new Time(System.currentTimeMillis()),"Dono da loja A", lojaA);

        var mockTransaction = List.of(primeiraTransacao,segundaTransacao,terceiraTransacao);
        when(transactionReportRepository.findAllByOrderByNomeDaLojaAscIdDesc()).thenReturn(mockTransaction);
        var transacaoReports = transacaoReportService.listTransactionsNameStore();
        //Verifica se o agrupamento de lojas esta sendo feito
        assertEquals(2,transacaoReports.size());

        transacaoReports.forEach(report -> {
            if(report.nomeDaLoja().equals(lojaA)){
                assertEquals(2,report.transacaoVOList().size());
                assertEquals(BigDecimal.valueOf(280),report.total());
                assertTrue(report.transacaoVOList().contains(segundaTransacao));
                assertTrue(report.transacaoVOList().contains(terceiraTransacao));
            }
            if(report.nomeDaLoja().equals(lojaB)){
                assertEquals(1,report.transacaoVOList().size());
                assertEquals(BigDecimal.valueOf(180),report.total());
                assertTrue(report.transacaoVOList().contains(primeiraTransacao));
            }

        });

    }
}
