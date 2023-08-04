package com.example.securitytraining.api;

import com.example.securitytraining.model.request.RegisterCustomerRequest;
import com.example.securitytraining.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public void register(@RequestBody RegisterCustomerRequest registerCustomerRequest) {
        customerService.registerCustomer(registerCustomerRequest);
    }
}
