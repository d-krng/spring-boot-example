package com.dk;

import com.dk.customer.Customer;
import com.dk.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SpringBootApplication
public class Main {


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner (CustomerRepository customerRepository) {


      return args -> {
          Faker faker = Faker.instance(new Locale("en-US"));
          String firstName = faker.name().firstName();
          String email = firstName+"@"+faker.internet().domainName();
          System.out.println(email);
          Customer fakeCustomer = new Customer(firstName,email.toLowerCase(),faker.number().numberBetween(1,100));
          List<Customer> customers = new ArrayList<>();
          customers.add(fakeCustomer);
          customerRepository.saveAll(customers);


      };
    }

}
