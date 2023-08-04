package com.example.securitytraining.model.request;

import lombok.Data;

import java.util.List;

@Data
public class RegisterCustomerRequest {
    private String email;
    private String password;
    private List<String> authorities;
}
