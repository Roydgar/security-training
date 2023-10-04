package com.example.securitytraining.security;

import com.example.securitytraining.model.entity.Customer;
import com.example.securitytraining.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerService customerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerService.findCustomerByEmail(username)
                .map(this::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private UserDetails toUserDetails(Customer customer) {
        return new User(customer.getEmail(), customer.getPassword(), mapAuthorities(customer.getAuthorities()));
    }

    private List<SimpleGrantedAuthority> mapAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
