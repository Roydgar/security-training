package com.example.securitytraining.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankingController {

    @GetMapping("/contact-us")
    public String contactUs() {
        return "Hello from contact us";
    }

    @GetMapping("/account")
    public String account() {
        return "Your account";
    }
}