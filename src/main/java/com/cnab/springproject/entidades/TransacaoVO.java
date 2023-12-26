package com.cnab.springproject.entidades;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public record TransacaoVO(Long id, Integer tipo, Date data, BigDecimal valor, Long cpf, String cartao, Time hora, String donoDaLoja, String nomeDaLoja) {


    public TransacaoVO withValor(BigDecimal valor){
       return new TransacaoVO(this.id(),this.tipo(),this.data(),
        valor,
        this.cpf(),this.cartao(),this.hora(),
        this.donoDaLoja(),this.nomeDaLoja());
    }

    public TransacaoVO withDate(String data) throws ParseException {
        var dateFormat = new SimpleDateFormat("yyyyMMdd");
        var date = dateFormat.parse(data);

        return new TransacaoVO(this.id(),this.tipo(),new Date (date.getTime()),
                this.valor(),
                this.cpf(),this.cartao(),this.hora(),
                this.donoDaLoja(),this.nomeDaLoja());
    }

    public TransacaoVO withHora(String hora) throws ParseException {
        var dateFormat = new SimpleDateFormat("HHmmss");
        var hour = dateFormat.parse(hora);

        return new TransacaoVO(this.id(),this.tipo(),this.data(),
                this.valor(),
                this.cpf(),this.cartao(),new Time (hour.getTime()),
                this.donoDaLoja(),this.nomeDaLoja());
    }


}
