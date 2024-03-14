package com.dk;

import com.dk.customer.Customer;
import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.Locale;
import java.util.UUID;

@Testcontainers
public abstract class AbstractTestcontainers {

    @BeforeAll
    static void beforeAll() {
        String url = postgreSQLContainer.getJdbcUrl();
        Flyway flyway = Flyway.configure().dataSource(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()).load();
        flyway.migrate();
        System.out.println(postgreSQLContainer.getJdbcUrl());
    }

    //Creating temp database
    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("dk-dao-unit-test")
            .withUsername("dk")
            .withPassword("password");

    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                postgreSQLContainer::getJdbcUrl); //() -> postgreSQLContainer.getJdbcUrl()
        registry.add("spring.datasource.username",
                postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password",
                postgreSQLContainer::getPassword);
    }

    private static DataSource getDataSource() {
        DataSourceBuilder builder = DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword());

        return builder.build();
    }

    protected static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

    protected static String randomGender() {
        double num = (int) (Math.random() * 10);
        if(num % 2 == 0) {
            return "MALE";
        }
        return "FEMALE";
    }
    protected static Customer createTestCustomer() {
        Faker faker = new Faker(new Locale("en-US"));
        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();

        return new Customer(
                firstname+" "+lastname,
                firstname+"."+lastname+"-"+UUID.randomUUID()+"@"+faker.internet().domainName().toLowerCase(),
                faker.number().numberBetween(16,99),
                randomGender()
        );
    }

}
