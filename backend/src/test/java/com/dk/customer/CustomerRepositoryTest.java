package com.dk.customer;

import com.dk.AbstractTestcontainers;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired // same as saying constructor
    private CustomerRepository underTest;


    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        Customer testCustomer = createTestCustomer();

        //When
        underTest.save(testCustomer);
        boolean existsCustomerWithEmail = underTest.existsCustomerByEmail(testCustomer.getEmail());
        Integer id = underTest.findAll().stream().filter(c-> c.getEmail().equals(testCustomer.getEmail())).findFirst().map(c->c.getId()).orElseThrow();

        Optional<Customer> actual = underTest.findById(id);

        //Then
        Assertions.assertThat(existsCustomerWithEmail).isTrue();
        Assertions.assertThat(actual).isPresent().hasValueSatisfying( c -> {
            Assertions.assertThat(c.getEmail()).isEqualTo(testCustomer.getEmail());
        });

    }

    @Test
    void existsCustomerById() {
        //Given

        //When

        //Then
    }

    @Test
    void findCustomerById() {
        //Given

        //When

        //Then
    }
}