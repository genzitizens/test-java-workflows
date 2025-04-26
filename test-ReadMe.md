
# Test Configuration for ExpenseApplication

This project uses Spring Boot + Mockito to safely test the application without connecting to real databases or MinIO instances.

## Test Environment Setup

In the `ExpenseApplicationTests` class:

- We disable actual DB auto-config:
  ```java
  @EnableAutoConfiguration(exclude = {
      DataSourceAutoConfiguration.class,
      HibernateJpaAutoConfiguration.class
  })
  ```
- We mock the following critical infrastructure beans:
    - `DataSource`
    - `MinioConfig`
    - `JobRepository`
    - `ExpenseWriter`
    - `PlatformTransactionManager`

```java
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
```

## Special: `ExpenseWriter` Mock Override

Because the real `ExpenseWriter` class would otherwise interfere with Batch Jobs,  
we explicitly mock it inside an internal `@Configuration`:

```java
@Configuration
static class TestOverrideBeans {
    @Bean
    public ExpenseWriter expenseWriter() {
        return mock(ExpenseWriter.class);
    }
}
```

This ensures that when Spring Batch jobs require an `ItemWriter<Expense>`,  
the mock is injected instead of the real implementation.

No real file writing.  
No DB transaction.  
Safe, fast unit test loading.

---

## Why This Setup?

Spring tries to eagerly load all dependencies during `@SpringBootTest` even for unrelated tests.  
By mocking these infrastructure beans, we short-circuit Spring's autowiring process.

This ensures that `contextLoads` passes without needing:
- A real PostgreSQL/MySQL instance
- A running MinIO server
- Actual JPA or Liquibase migrations

---

# Summary

| Purpose               | Implementation                             |
|:----------------------|:------------------------------------------|
| Disable real DB        | `@EnableAutoConfiguration(exclude = {...})` |
| Provide Mock Beans     | `@MockitoBean` everywhere                  |
| Safe override of writer| `@Configuration + @Bean (mock)`           |

---

# Bonus

If you want to fully isolate even further,  
you can in the future split tests with profiles like `@ActiveProfiles("test")` or even `@TestConfiguration` files if needed.

But for now,  
if it works, it works.
