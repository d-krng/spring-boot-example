package com.dk.customer;

import com.dk.AbstractTestcontainers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoAssertionError;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJPADataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception{
        //After test is done we have a fresh mock to work on
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //Given -> not needed because actual method does not want any param
        //When -> invoke method which we want to test
        underTest.selectAllCustomers();

        //Then
        Mockito.verify(customerRepository).findAll();
        //Why do we need to test JPADataAccessService even the methods used are provided from JPA -> for instance someone could change in JPADataAccessService our method
        //Instance: change in JPADataAccessService selectAllCustomers()-> customerRepository.findAll() to customerRepository.deleteAll();
    }

    @Test
    void selectCustomerById() {
        //Given
        Integer id = 100000;
        //When
        underTest.selectCustomerById(id);
        //Then
        Mockito.verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        //Given
        Customer testCustomer = createTestCustomer();

        //When
        underTest.insertCustomer(testCustomer);

        //Then
        Mockito.verify(customerRepository).save(testCustomer);
    }

    @Test
    void existsCustomerWithEmail() {
        //Given
        String email = createTestCustomer().getEmail();

        //When
        underTest.existsCustomerWithEmail(email);
        //Then
        Mockito.verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void deleteCustomerById() {
        //Given
        int id = 1;
        //When
        underTest.deleteCustomerById(id);
        //Then
        Mockito.verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        //Given
        Customer testCustomer = createTestCustomer();

        //When
        underTest.updateCustomer(testCustomer);

        //Then
        Mockito.verify(customerRepository).saveAndFlush(testCustomer);
    }

    @Test
    void existsCustomerById() {
        //Given
        int id = 1;
        //When
        underTest.existsCustomerById(id);
        //Then
        Mockito.verify(customerRepository).existsById(id);
    }
}