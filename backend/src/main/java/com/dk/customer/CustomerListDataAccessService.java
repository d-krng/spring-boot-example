package com.dk.customer;

import com.dk.exception.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{

    //CRUD Creat Read Update Delete
    //DB
    private static List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer jamila = new Customer(
                2,
                "jamila",
                "jamila@gmail.com",
                19,
                "FEMALE");

        Customer alex = new Customer(
                1,
                "alex",
                "alex@gmail.com",
                18,
                "MALE");

        customers.add(alex);
        customers.add(jamila);

    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        return  customers.stream().filter(
                customer -> customer.getId().equals(customerId)).findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {

    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        /*Optional<Customer> c = customers.stream().filter(customer -> customer.getEmail().equals(email)).findFirst();
         if (c != null) {
             return true;
         }
         return false;*/
        return customers.stream().anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public void deleteCustomerById(Integer id) {

    }

    @Override
    public void updateCustomer(Customer customer) {

    }

    @Override
    public boolean existsCustomerById(Integer id) {
        return false;
    }


}
