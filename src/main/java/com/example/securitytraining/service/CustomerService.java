package com.example.securitytraining.service;

import com.example.securitytraining.model.entity.Customer;
import com.example.securitytraining.model.request.RegisterCustomerRequest;
import com.example.securitytraining.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerCustomer(RegisterCustomerRequest customerRequest) {
        var encodedPassword = passwordEncoder.encode(customerRequest.getPassword());

        var customer = new Customer();
        customer.setEmail(customerRequest.getEmail());
        customer.setPassword(encodedPassword);
        customer.setAuthorities(customerRequest.getAuthorities());

        customerRepository.save(customer);
    }

    public Optional<Customer> findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}
