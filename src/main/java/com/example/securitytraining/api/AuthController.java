package com.example.securitytraining.api;

import com.example.securitytraining.model.request.LoginRequest;
import com.example.securitytraining.security.JwtTokenProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Profile("auth.jwt")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtTokenProcessor jwtTokenProcessor;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        var userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        var usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        var authentication = authenticationManager.authenticate(usernamePasswordAuthentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var jwtToken = jwtTokenProcessor.createJwtToken(userDetails);
        return ResponseEntity.ok()
                .header(AUTHORIZATION, jwtToken)
                .build();
    }
}
