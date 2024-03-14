package com.dk.customer;

public record CustomerUpdate(
        String name,
        String email,
        Integer age,
        String gender) {
}
