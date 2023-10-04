package com.example.securitytraining.config;

import com.example.securitytraining.filter.JwtVerificationFilter;
import com.example.securitytraining.security.JwtTokenProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Set;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
@Profile("auth.jwt")
public class JwtSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfiguration corsConfiguration,
                                                   JwtTokenProcessor jwtTokenProcessor) throws Exception {
        var permitAllUris = new String[] {"/contact-us", "/customer/register", "/auth/login"};
        var jwtVerificationFilter = new JwtVerificationFilter(jwtTokenProcessor, Set.of(permitAllUris));

        return httpSecurity
                // disable jsessionid
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(request -> corsConfiguration))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(permitAllUris).permitAll()
                        .requestMatchers("/account").authenticated()
                        .requestMatchers("/admin-panel").hasRole("ADMIN"))
                .addFilterBefore(jwtVerificationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        var config = new CorsConfiguration();

        config.setAllowedOrigins(singletonList("http://localhost:4200"));
        config.setAllowedMethods(singletonList("*"));
        // Make Authorization header with JWT token exposed by CORS
        config.setAllowedHeaders(singletonList(AUTHORIZATION));
        config.setExposedHeaders(singletonList(AUTHORIZATION));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        return config;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
