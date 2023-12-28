package com.cnab.springproject.services.reports;

import com.cnab.springproject.entidades.TransacaoReport;
import com.cnab.springproject.repositories.TransactionReportRepository;
import com.cnab.springproject.utils.enums.TipoTransacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class TransacaoReportService {

    @Autowired
    private TransactionReportRepository transactionReportRepository;

    public List<TransacaoReport> listTransactionsNameStore(){
       var transacoes =  transactionReportRepository.findAllByOrderByNomeDaLojaAscIdDesc();
       var reportMap = new LinkedHashMap<String,TransacaoReport>();

       transacoes.forEach(transacao -> {
           String nomeDaLoja = transacao.nomeDaLoja();
           var tipoTransacao = TipoTransacao.findByTipo(transacao.tipo());
           BigDecimal valor = transacao.valor().multiply(tipoTransacao.getSinal());
           reportMap.compute(nomeDaLoja,(key,existingReport) -> {
               var report = (existingReport != null) ? existingReport : new TransacaoReport(key, BigDecimal.ZERO, new ArrayList<>());
            return report.addTotal(valor).addTransacaoList(transacao.withValor(valor));
           });
       });

        return new ArrayList<>(reportMap.values());

    }
}
