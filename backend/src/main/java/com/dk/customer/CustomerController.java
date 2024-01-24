package com.dk.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //  @RequestMapping(path = "api/v1/customers", method = RequestMethod.GET)
    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @RequestMapping(path = "{id}")
    public Customer getCustomer(@PathVariable(name = "id") Integer id) {
        return customerService.getCustomer(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void registerCustomer(
            @RequestBody CustomerRegistrationRequest request) {

        customerService.addCustomer(request);

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "{id}")
    public void deactivateCustomer(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
    }

/*    @RequestMapping(method = RequestMethod.PUT, path = "{id}")
    public void updateCustomerData(
            @PathVariable(required = false) String name,
            @PathVariable(required = false) String email,
            @PathVariable(required = false) Integer age,
            @PathVariable(required = true) Integer id) {

        customerService.updateCustomerName(id, name);

    }*/

    @RequestMapping(method = RequestMethod.PUT, path = "{id}")
    public void updateCustomerInfo(@RequestBody CustomerUpdate customerUpdate, @PathVariable(name = "id") Integer id) {
            customerService.updateCustomerById(id, customerUpdate);

    }

}
