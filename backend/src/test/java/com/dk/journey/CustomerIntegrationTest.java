package com.dk.journey;

import com.dk.customer.Customer;
import com.dk.customer.CustomerRegistrationRequest;
import com.dk.customer.CustomerUpdate;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {


    //WebTestClient -> our Postman
    @Autowired
    private WebTestClient webTestClient;

    private static final String CUSTOMER_URI = "api/v1/customers";

    @Test
    void canRegisterACustomer() {
        //create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.firstName();
        String email = UUID.randomUUID() +"@ayeth.com";
        int age = faker.number().numberBetween(10,100);
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                name,
                email,
                age
        );

        //send a post request
        webTestClient
                .post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that customer is present
        assert allCustomers != null;
        Customer customerTest = allCustomers.stream().filter(c-> c.getEmail().equals(email)).findFirst().orElseThrow();

        Assertions.assertThat(allCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(customerTest);
        //get customer by id
        int id = allCustomers.stream().filter(c->c.getEmail().equals(email)).findFirst().map(c -> c.getId()).orElseThrow();
        System.out.println(id);

       Customer actual = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(CUSTOMER_URI+"/{id}").build(id))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        assert actual != null;
        System.out.println(actual.getEmail());
        System.out.println(actual.getId());


       Assertions.assertThat(actual.getEmail()).isEqualTo(email);
    }

    @Test
    void canDeactivateCustomer() {

        String email = "name@test.com";

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                "name",
                        email,
                18
        );

        //send a post request
        webTestClient
                .post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        int id = allCustomers.stream().filter(c->c.getEmail().equals(email)).findFirst().map(c->c.getId()).orElseThrow();
        System.out.println(id);

        //find by id
        Customer customerTest = allCustomers.stream().filter(c-> c.getEmail().equals(email)).findFirst().orElseThrow();

        //delete Customer
        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path(CUSTOMER_URI+"/{id}").build(id))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        //call to endpoint to verify that customer is deleted
        List<Customer> allCustomersAfterDelete = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(allCustomersAfterDelete).doesNotContainSequence(customerTest);

    }

    @Test
    void canUpdateCustomer() {

        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.firstName();
        String email = UUID.randomUUID() +"@ayeth.com";
        int age = faker.number().numberBetween(10,100);

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                name,
                email,
                age
        );

        //send a post request
        webTestClient
                .post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        int id = allCustomers.stream().filter(c->c.getEmail().equals(email)).findFirst().map(Customer::getId).orElseThrow();

        //Update Customer
        CustomerUpdate customerUpdate = new CustomerUpdate(
                "DK",
                email,
                age
        );

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path(CUSTOMER_URI+"/{id}").build(id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerUpdate),CustomerUpdate.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get Customer and check if Name was updated

        List<Customer> allCustomersAfterUpdate = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        assert allCustomersAfterUpdate != null;
        Customer actual = allCustomersAfterUpdate.stream().filter(c->c.getEmail().equals(email)).findFirst().orElseThrow();

        Assertions.assertThat(actual.getName()).isEqualTo("DK");
    }
}
