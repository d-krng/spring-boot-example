package com.dk.customer;

import com.dk.AbstractTestcontainers;
import com.dk.exception.DublicateResourceException;
import com.dk.exception.InvalidDataException;
import com.dk.exception.ResourceNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest extends AbstractTestcontainers {

    private AutoCloseable autoCloseable;
    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
       // this is not needed because of Annotation over class autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerService(customerDao);
    }

    @AfterEach
    void tearDown() throws Exception {
        //After test is done we have a fresh mock to work on
        // not needed too autoCloseable.close();
    }

    @Test
    void getAllCustomers() {
        //Given

        //When
        underTest.getAllCustomers();

        //Then
        Mockito.verify(customerDao).selectAllCustomers();
    }

    @Test
    void getCustomer() {
        //Given
        int id = 1;
        Customer customerTest = createTestCustomer();
        customerTest.setId(id);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customerTest));
        //When
        Customer actual = underTest.getCustomer(id);
        //Then
        Mockito.verify(customerDao).selectCustomerById(id);
        Assertions.assertThat(actual).isEqualTo(customerTest);

    }

    @Test
    void willThrowExceptionWhenGetCustomerReturnEmptyOptional() {
        //Given
        int id = 1;
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        //When
        //Then
        Assertions.assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User with ID: %s not found".formatted(id));

    }

    @Test
    void addCustomer() {
        //Given
       Customer customerTest = createTestCustomer();
       Mockito.when(customerDao.existsCustomerWithEmail(customerTest.getEmail())).thenReturn(false);
        //When
        underTest.addCustomer(new CustomerRegistrationRequest(customerTest.getName(),customerTest.getEmail(),customerTest.getAge()));
        //Then
        Mockito.verify(customerDao).insertCustomer(customerTest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        ArgumentCaptor<CustomerService> customerServiceArgumentCaptor = ArgumentCaptor.forClass(CustomerService.class);

        Mockito.verify(customerDao).insertCustomer(customerArgumentCaptor.capture());// Wir prüfen hier ob der richtige Datentyp übergeben wurde und wenn ja dann wird der Wert aus customerTest übergeben
        //System.out.println("Hier: " + customerArgumentCaptor.getValue().getEmail());
        //System.out.println("Hier: " + customerArgumentCaptor.getValue().getId());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        Assertions.assertThat(capturedCustomer).isEqualTo(customerTest);

    }

    @Test
    void willThrowExceptionIfAddCustomerEmailIsDublicate() {
        //Given
        Customer customerTest = createTestCustomer();
        Mockito.when(customerDao.existsCustomerWithEmail(customerTest.getEmail())).thenReturn(true);
        //When
        //Then

        Assertions.assertThatThrownBy(()-> underTest.addCustomer(new CustomerRegistrationRequest(customerTest.getName(),customerTest.getEmail(),customerTest.getAge())))
                .isInstanceOf(DublicateResourceException.class)
                .hasMessage("This email: %s already used".formatted(customerTest.getEmail()));

    }

    @Test
    void deleteCustomer() {
        //Given
        int id = 1;
        Customer customerTest = createTestCustomer();
        customerTest.setId(id);
        //Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customerTest));
        Mockito.when(customerDao.existsCustomerById(id)).thenReturn(true);
        //When
        underTest.deleteCustomer(id);
        //Then
        Mockito.verify(customerDao).deleteCustomerById(id);


    }

    @Test
    void willThrowExceptionIfDeleteCustomerEmptyOptional() {
        //Given
        int id = 1;
        Customer customerTest = createTestCustomer();
        customerTest.setId(id);

        Mockito.when(customerDao.existsCustomerById(id)).thenReturn(false);
        //When
        //Then
        Assertions.assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id: %s not found".formatted(id));

        Mockito.verify(customerDao,Mockito.never()).insertCustomer(customerTest);
    }

    @Test
    void updateCustomerById() {
        //Given
        int id =1;
        Customer customerTest = createTestCustomer();
        Mockito.when(customerDao.existsCustomerById(id)).thenReturn(true);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customerTest));
        //When
        underTest.updateCustomerById(
                id,
                new CustomerUpdate(
                        "Update",
                        customerTest.getEmail(),
                        customerTest.getAge()
                )
        );

        //Then
        Mockito.verify(customerDao).updateCustomer(customerTest);

    }

    @Test
    void willThrowExceptionIfUpdateCustomerByIdReturnNoChanges() {
        //Given
        int id =1;
        Customer customerTest = createTestCustomer();
        Mockito.when(customerDao.existsCustomerById(id)).thenReturn(true);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customerTest));
        //When
        //Then
        Assertions.assertThatThrownBy(() -> underTest.updateCustomerById(
                id,
                new CustomerUpdate(
                        customerTest.getName(),
                        customerTest.getEmail(),
                        customerTest.getAge()
                )
        )).isInstanceOf(InvalidDataException.class).hasMessage("no data changes found");
    }

    @Test
    void willThrowExceptionIfUpdateCustomerByIdReturnEmpty() {
        //Given
        int id = 1;
        Customer customerTest = createTestCustomer();
        Mockito.when(customerDao.existsCustomerById(id)).thenReturn(false);
        //When
        //Then
        Assertions.assertThatThrownBy(() -> underTest.updateCustomerById(
                id,
                new CustomerUpdate(
                        customerTest.getName(),
                        customerTest.getEmail(),
                        customerTest.getAge()
                )
        )).isInstanceOf(ResourceNotFoundException.class).hasMessage("Customer with id:[%s] not found".formatted(id));
    }
}