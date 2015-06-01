package org.saumilp.tutorials.spring;

import org.saumilp.tutorials.spring.models.Person;
import org.saumilp.tutorials.spring.processor.PersonItemProcessor;
import org.saumilp.tutorials.spring.tokenizer.PersonFixedLengthTokenizer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Batch Configuration Class
 */
@Configuration
@EnableBatchProcessing
@ComponentScan
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
public class BatchConfiguration {
    @Value("${database.driver}")
    private String databaseDriver;

    @Value("${database.url}")
    private String databaseUrl;

    @Value("${database.username}")
    private String databaseUser;

    @Value("${database.password}")
    private String databasePassword;

    @Bean
    public ItemReader<Person> reader(){
        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
        reader.setResource(new ClassPathResource("PersonData.txt"));
        reader.setLineMapper(new DefaultLineMapper<Person>(){{
           setLineTokenizer(new PersonFixedLengthTokenizer());
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>(){{
                setTargetType(Person.class);
            }});
        }});
        return reader;
    }

    @Bean
    public ItemWriter<Person> writer(){
        JpaItemWriter writer = new JpaItemWriter<Person>();
        writer.setEntityManagerFactory(entityManagerFactory().getObject());
        return writer;
    }

    @Bean
    public Job importPerson(JobBuilderFactory factory, Step s1){
        return factory.get("import")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public ItemProcessor<Person, Person> processor(){
        return new PersonItemProcessor();
    }

    @Bean
    public Step step1(StepBuilderFactory factory, ItemReader<Person> reader,
                      ItemWriter<Person> writer, ItemProcessor<Person, Person> processor){
        return factory.get("step1")
                .<Person,Person>chunk(1000)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(databaseDriver);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUser);
        dataSource.setPassword(databasePassword);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setPackagesToScan("org.saumilp.tutorials.spring");
        lef.setDataSource(dataSource());
        lef.setJpaVendorAdapter(jpaVendorAdapter());
        lef.setJpaProperties(new Properties());
        return lef;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);
        adapter.setGenerateDdl(true);
        adapter.setShowSql(false);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
        return adapter;
    }

    @Bean
    public JobExplorer jobExplorer(DataSource dataSource) throws Exception {
        final JobExplorerFactoryBean factory = new JobExplorerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTablePrefix("batchdb.BATCH_");
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public JobRepository jobRepository(DataSource dataSource) throws Exception {
        final JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTablePrefix("batchdb.BATCH_");
        factory.setTransactionManager(transactionManager(dataSource));
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    private PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
