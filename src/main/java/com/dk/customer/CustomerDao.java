package com.dk.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer customerId);
    void insertCustomer(Customer customer);
    boolean existsCustomerWithEmail(String email);

    void deleteCustomerById(Integer id);

    void updateCustomer(Customer customer);
    boolean existsCustomerById(Integer id);

}
