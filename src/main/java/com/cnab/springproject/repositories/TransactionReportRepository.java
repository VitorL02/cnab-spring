package com.cnab.springproject.repositories;

import com.cnab.springproject.entidades.TransacaoVO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionReportRepository extends CrudRepository<TransacaoVO,Long> {

    List<TransacaoVO> findAllByOrderByNomeDaLojaAscIdDesc();
}
