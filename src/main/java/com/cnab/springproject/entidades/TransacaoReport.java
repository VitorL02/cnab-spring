package com.cnab.springproject.entidades;

import java.math.BigDecimal;
import java.util.List;

public record TransacaoReport(String nomeDaLoja, BigDecimal total, List<TransacaoVO>transacaoVOList) {


    public TransacaoReport addTotal(BigDecimal valor){
        return new TransacaoReport(nomeDaLoja,total.add(valor),transacaoVOList);
    }

    public TransacaoReport addTransacaoList(TransacaoVO transacaoVO){
        transacaoVOList.add(transacaoVO);
        return new TransacaoReport(nomeDaLoja,total,transacaoVOList);
    }

}
