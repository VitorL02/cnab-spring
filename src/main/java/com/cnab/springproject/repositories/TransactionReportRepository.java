package com.cnab.springproject.repositories;

import com.cnab.springproject.entidades.Transacao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionReportRepository extends CrudRepository<Transacao,Long> {

    List<Transacao> findAllByOrderByNomeDaLojaAscIdDesc();
}
