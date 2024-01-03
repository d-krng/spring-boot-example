package com.dk.customer;

import com.dk.AbstractTestcontainers;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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
       Integer id = underTest.selectAllCustomers().stream()
               .filter(c -> c.getEmail().equals(email))
               .findFirst()
               .map(c -> c.getId())
               .orElseThrow();

       Optional<Customer> ex = underTest.selectCustomerById(id);


        //Then
    }

    @Test
    void insertCustomer() {
        //Given

        //When

        //Then
    }

    @Test
    void existsCustomerWithEmail() {
        //Given

        //When

        //Then
    }

    @Test
    void deleteCustomerById() {
        //Given

        //When

        //Then
    }

    @Test
    void updateCustomer() {
        //Given

        //When

        //Then
    }

    @Test
    void existsCustomerById() {
        //Given

        //When

        //Then
    }
}