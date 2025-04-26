package com.example.expense;

import com.example.expense.config.MinioConfig;
import com.example.expense.jobs.writer.ExpenseWriter;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static org.mockito.Mockito.mock;

@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
        org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
})
@SpringBootTest
@ActiveProfiles("test")
class ExpenseApplicationTests {

    // Mocks to prevent real DB usage
    @MockitoBean
    private DataSource dataSource;

    @MockitoBean
    private MinioConfig minioConfig;
    @MockitoBean
    private JobRepository jobRepository;

    @MockitoBean
    private ExpenseWriter expenseWriter;

    @MockitoBean
    private PlatformTransactionManager platformTransactionManager;

    @Test
    void contextLoads() {
    }

    @Configuration
    static class TestOverrideBeans {

        @Bean
        public ExpenseWriter expenseWriter() {
            return mock(ExpenseWriter.class);
        }
    }
}