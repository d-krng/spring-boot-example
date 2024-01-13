package com.dk.customer;

import com.dk.exception.DublicateResourceException;
import com.dk.exception.InvalidDataException;
import com.dk.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerDao customerDao;


    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.selectCustomerById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with ID: %s not found".formatted(id)));

    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        //check if email exist
        //add
        if (customerDao.existsCustomerWithEmail(customerRegistrationRequest.email())) {
            throw new DublicateResourceException("This email: %s already used".formatted(customerRegistrationRequest.email()));
        } else {
            customerDao.insertCustomer(
                    new Customer(
                            customerRegistrationRequest.name(),
                            customerRegistrationRequest.email(),
                            customerRegistrationRequest.age()));
        }

    }

    public void deleteCustomer(Integer id) {
        //Look for customer with id
        if (customerDao.existsCustomerById(id)) { //Falls was bricht habe hier was verändert
             customerDao.deleteCustomerById(id);
        } else {
            throw new ResourceNotFoundException("Customer with id: %s not found".formatted(id));
        }

    }


    public void updateCustomerById(Integer id, CustomerUpdate customerUpdate) {

        boolean changes = false;

        if (customerDao.existsCustomerById(id)/*selectCustomerById(id).isPresent()*/) {

            Customer customerToUpdate = customerDao.selectCustomerById(id).get();
            if (customerUpdate.name() != null && !(customerUpdate.name().equals(customerToUpdate.getName()))) {
                customerToUpdate.setName(customerUpdate.name());
                changes = true;
            }
            if (customerUpdate.age() != null && !(customerUpdate.age().equals(customerToUpdate.getAge()))) {
                customerToUpdate.setAge(customerUpdate.age());
                changes = true;
            }
            if (customerUpdate.email() != null && !(customerUpdate.email().equals(customerToUpdate.getEmail()))) {
                if (customerDao.existsCustomerWithEmail(customerUpdate.email())) {
                    throw new DublicateResourceException("Email is already used");
                }
                customerToUpdate.setEmail(customerUpdate.email());
                changes = true;
            }
            if (!changes) {
                throw new InvalidDataException("no data changes found");
            }

            customerDao.updateCustomer(customerToUpdate);
        }else {
            throw new ResourceNotFoundException("Customer with id:[%s] not found".formatted(id));
        }

    }

}
