package com.cnab.springproject;

import com.cnab.springproject.dtos.TransacaoCnabDTO;
import com.cnab.springproject.entidades.TransacaoVO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Configuration
public class BatchConfig {
    @Autowired
    private PlatformTransactionManager platformTransactionManager;


    @Bean
    Job job(Step step,JobRepository jobRepository){
        return new JobBuilder("job",jobRepository).start(step).incrementer(new RunIdIncrementer()).build();
    }

    @Bean
    Step step(JobRepository jobRepository, ItemReader<TransacaoCnabDTO> itemReader, ItemProcessor<TransacaoCnabDTO,TransacaoVO> itemProcessor, ItemWriter<TransacaoVO> itemWriter){
        return new StepBuilder("step",jobRepository).
                <TransacaoCnabDTO,TransacaoVO>chunk(1000,platformTransactionManager)
                .reader(itemReader).processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    FlatFileItemReader<TransacaoCnabDTO>reader(){
        return new FlatFileItemReaderBuilder<TransacaoCnabDTO>().name("reader").resource(new FileSystemResource(
                "files/CNAB.txt"))
                .fixedLength()
                .columns(new Range(1,1), new Range(2,9),
                        new Range(10,19), new Range(20,30),
                        new Range(31,42),new Range(43,48),
                        new Range(49,62), new Range(63,80))
                .names("tipo","data","valor","cpf","cartao","hora","donoDaLoja","nomeDaLoja")
                .targetType(TransacaoCnabDTO.class)
                .build();

    }

    @Bean
    ItemProcessor<TransacaoCnabDTO,TransacaoVO> processor(){
        return item ->{
            //Wither pattern - Usado para objetos imutavies para recriar somente a propriedade necessaria,recriando a transação solicitada
            var transacao = new TransacaoVO(null,item.tipo(),null,item.valor().divide(BigDecimal.valueOf(100)),
                    item.cpf(),item.cartao(),null,item.donoDaLoja().trim(), item.nomeDaLoja().trim())
                    .withDate(item.data())
                    .withHora(item.hora());
            return  transacao;
        };

    }

    @Bean
    JdbcBatchItemWriter<TransacaoVO> writer(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<TransacaoVO>().dataSource(dataSource).sql("INSERT INTO transacao(tipo,data,valor,cpf,cartao,hora,dono_loja,nome_loja)" +
                "VALUES (:tipo, :data, :valor,:cpf,:cartao,:hora,:donoDaLoja,:nomeDaLoja)").beanMapped().build();
    }


}
