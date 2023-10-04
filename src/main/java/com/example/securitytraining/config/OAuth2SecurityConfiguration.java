package com.example.securitytraining.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Profile("auth.oAuth2")
@Configuration
public class OAuth2SecurityConfiguration {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(requests -> requests.anyRequest().authenticated())
                .oauth2Login(Customizer.withDefaults())
                .build();
    }

    // or in application.yaml:
    // spring:
    //  security:
    //    oauth2:
    //      client:
    //        registration:
    //          github:
    //            clientId: github-client-id
    //            clientSecret: github-client-secret
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        var oAuthProvider = CommonOAuth2Provider.GITHUB.getBuilder("github")
                .clientId("")
                .clientSecret("")
                .build();
        return new InMemoryClientRegistrationRepository(oAuthProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
