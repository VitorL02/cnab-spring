package com.cnab.springproject.dtos;

import java.math.BigDecimal;

public record TransacaoCnabDTO(Integer tipo, String data, BigDecimal valor,Long cpf,String cartao,String hora,String donoDaLoja,String nomeDaLoja) {}
