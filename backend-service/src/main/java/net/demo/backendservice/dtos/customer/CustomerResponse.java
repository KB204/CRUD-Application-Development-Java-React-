package net.demo.backendservice.dtos.customer;


public record CustomerResponse(
        String firstName,
        String lastName,
        String email,
        String phone,
        String address) {}
