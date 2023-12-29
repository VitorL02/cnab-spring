package com.cnab.springproject;

import com.cnab.springproject.dtos.TransacaoCnabDTO;
import com.cnab.springproject.entidades.Transacao;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Configuration
public class BatchConfig {
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private  JobRepository jobRepository;

    @Bean
    Job job(Step step,JobRepository jobRepository){
        return new JobBuilder("job",jobRepository).start(step).incrementer(new RunIdIncrementer()).build();
    }

    @Bean
    Step step(JobRepository jobRepository, ItemReader<TransacaoCnabDTO> itemReader, ItemProcessor<TransacaoCnabDTO, Transacao> itemProcessor, ItemWriter<Transacao> itemWriter){
        return new StepBuilder("step",jobRepository).
                <TransacaoCnabDTO, Transacao>chunk(1000,platformTransactionManager)
                .reader(itemReader).processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @StepScope
    @Bean
    FlatFileItemReader<TransacaoCnabDTO>reader(@Value("#{jobParameters['cnabFile']}")Resource resource){
        return new FlatFileItemReaderBuilder<TransacaoCnabDTO>().name("reader").resource(resource)
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
    ItemProcessor<TransacaoCnabDTO, Transacao> processor(){
        return item ->{
            //Wither pattern - Usado para objetos imutavies para recriar somente a propriedade necessaria,recriando a transação solicitada
            var transacao = new Transacao(null,item.tipo(),null,item.valor().divide(BigDecimal.valueOf(100)),
                    item.cpf(),item.cartao(),null,item.donoDaLoja().trim(), item.nomeDaLoja().trim())
                    .withDate(item.data())
                    .withHora(item.hora());
            return  transacao;
        };

    }

    @Bean
    JdbcBatchItemWriter<Transacao> writer(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<Transacao>().dataSource(dataSource).sql("INSERT INTO transacao(tipo,data,valor,cpf,cartao,hora,dono_loja,nome_loja)" +
                "VALUES (:tipo, :data, :valor,:cpf,:cartao,:hora,:donoDaLoja,:nomeDaLoja)").beanMapped().build();
    }

    @Bean
    JobLauncher jobLauncherAsync(JobRepository jobRepository) throws Exception {
        var  jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        //Setta o job para ser assincrono
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;

    }

}
