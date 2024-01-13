package com.dk.customer;

import com.dk.AbstractTestcontainers;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerRowMapperTest extends AbstractTestcontainers {

    @Mock
    private RowMapper<Customer> customerRowMapper;
    @Mock
    private ResultSet resultSet;

    private CustomerRowMapper underTest;

    @BeforeEach
    void setUp() {
        underTest= new CustomerRowMapper();
    }

    @Test
    void mapRow() throws SQLException {
        //Given
        Customer customerTest = createTestCustomer();
        Mockito.when(resultSet.getInt("id")).thenReturn(1);
        Mockito.when(resultSet.getString("name")).thenReturn(customerTest.getName());
        Mockito.when(resultSet.getString("email")).thenReturn(customerTest.getEmail());
        Mockito.when(resultSet.getInt("age")).thenReturn(customerTest.getAge());
        //When
        Customer actual = underTest.mapRow(resultSet,1);

        //Then
        Assertions.assertThat(actual.getId()).isEqualTo(1);
        Assertions.assertThat(actual.getEmail()).isEqualTo(customerTest.getEmail());
    }
}