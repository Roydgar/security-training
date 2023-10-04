package com.example.securitytraining.security.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@Builder
public class JwtTokenBody {
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
}
