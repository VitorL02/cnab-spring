package com.cnab.springproject.entidades;

import java.math.BigDecimal;
import java.util.List;

public record TransacaoReport(String nomeDaLoja, BigDecimal total, List<Transacao> transacaoList) {


    public TransacaoReport addTotal(BigDecimal valor){
        return new TransacaoReport(nomeDaLoja,total.add(valor), transacaoList);
    }

    public TransacaoReport addTransacaoList(Transacao transacao){
        transacaoList.add(transacao);
        return new TransacaoReport(nomeDaLoja,total, transacaoList);
    }

}
