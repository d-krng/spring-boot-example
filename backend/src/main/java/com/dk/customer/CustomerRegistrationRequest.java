package com.dk.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {


}
