package com.example.securitytraining.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Customer {

    @Id
    private String id;

    private String email;
    private String password;
    private List<String> authorities;
}
