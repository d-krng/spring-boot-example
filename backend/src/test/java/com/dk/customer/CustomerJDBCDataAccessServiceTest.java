package com.dk.customer;

import com.dk.AbstractTestcontainers;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.relational.core.sql.In;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        //We want a new fresh object of the CustomerJDBCDataAccessService before every test
        underTest = new CustomerJDBCDataAccessService( getJdbcTemplate(),customerRowMapper);

    }

    @Test
    void selectAllCustomers() {
        //Given
        Customer customerTest = createTestCustomer();
        underTest.insertCustomer(customerTest);

        //When
        List<Customer> customers = underTest.selectAllCustomers();

        //Then
        Assertions.assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        //Given
        Customer customerTest = createTestCustomer();
        underTest.insertCustomer(customerTest);

        //When
       String email = customerTest.getEmail();
       Integer id = underTest.selectAllCustomers()
               .stream()
               .filter(c -> c.getEmail().equals(email))
               .findFirst()
               .map(c -> c.getId())
               .orElseThrow();

       Optional<Customer> actualCustomer = underTest.selectCustomerById(id);



        //Then
        Assertions.assertThat(actualCustomer).isPresent().hasValueSatisfying(c -> {

            Assertions.assertThat(c.getId()).isEqualTo(id);
            Assertions.assertThat(c.getName()).isEqualTo(customerTest.getName());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        //Given
        int id = -1;
        //When
        var actual = underTest.selectCustomerById(id);
        //Then
        Assertions.assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        //Given
        Customer testCustomer = createTestCustomer();

        //When
        underTest.insertCustomer(testCustomer);

        String email = testCustomer.getEmail();
        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .map(c -> c.getId())
                .orElseThrow();

        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        Assertions.assertThat(actual).hasValueSatisfying( c -> {
            Assertions.assertThat(c.getId()).isEqualTo(id);
            Assertions.assertThat(c.getEmail()).isEqualTo(testCustomer.getEmail());
            Assertions.assertThat(c.getName()).isEqualTo(testCustomer.getName());
        });

    }

    @Test
    void existsCustomerWithEmail() {
        //Given
        Customer testCustomer = createTestCustomer();

        //When
        underTest.insertCustomer(testCustomer);

        String email = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(testCustomer.getEmail()))
                .findFirst()
                .map(c -> c.getEmail())
                .orElseThrow();
        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(testCustomer.getEmail()))
                .findFirst()
                .map(c -> c.getId())
                .orElseThrow();

        boolean actualReturn =  underTest.existsCustomerWithEmail(email);

        //Then
        Assertions.assertThat(actualReturn).isTrue();
        Assertions.assertThat(underTest.selectCustomerById(id)).hasValueSatisfying( c -> {
            Assertions.assertThat(c.getEmail()).isEqualTo(email);
        });

    }

    @Test
    void willReturnEmptyWhenIdIsNotPresent() {
        //Given
        int id = -1;
        //When

        //Then
    }

    @Test
    void deleteCustomerById() {
        //Given maybe second test for thid method
        Customer testCustomer = createTestCustomer();

        //When
        underTest.insertCustomer(testCustomer);
        Assertions.assertThat(underTest.selectAllCustomers()).isNotEmpty();
        int id = underTest.selectAllCustomers()
                        .stream()
                                .filter(c -> c.getEmail().equals(testCustomer.getEmail()))
                                        .findFirst()
                                                .map(c -> c.getId())
                                                        .orElseThrow();
        underTest.deleteCustomerById(id);
        Optional<Customer> actual = underTest.selectCustomerById(id);
        //Then
        Assertions.assertThat(actual).isEmpty();
    }

    @Test
    void updateCustomer() {
        //Given
        Customer testCustomer = createTestCustomer();
        UUID unique = UUID.randomUUID();
        String name = "test"+unique;
        String email = "test@"+unique;
        Integer age = 69;

        //When
        underTest.insertCustomer(testCustomer);

        testCustomer.setEmail(email);
        underTest.updateCustomer(testCustomer);


        //Then
        Assertions.assertThat(testCustomer.getEmail()).isEqualTo(email);
    }

    @Test
    void existsCustomerById() {
        //Given
        Customer testCustomer = createTestCustomer();
        //When
        underTest.insertCustomer(testCustomer);
        Integer id = underTest.selectAllCustomers().stream().filter(c->c.getEmail().equals(testCustomer.getEmail())).findFirst().map(c->c.getId()).orElseThrow();
        boolean existsCustomerWithId = underTest.existsCustomerById(id);

        //Then
        Assertions.assertThat(existsCustomerWithId).isTrue();
    }
}