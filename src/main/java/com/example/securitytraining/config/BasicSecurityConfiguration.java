package com.example.securitytraining.config;

import com.example.securitytraining.filter.CsrfCookieFilter;
import com.example.securitytraining.filter.RequestValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import static java.util.Collections.singletonList;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("auth.basic")
public class BasicSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfiguration corsConfiguration) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(request -> corsConfiguration))
//                .csrf(csrf -> csrf.ignoringRequestMatchers("/contact-us", "/register")
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new RequestValidationFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/contact-us", "/customer/register").permitAll()
                        .requestMatchers("/account").hasAuthority("VIEW_ACCOUNT")
                        .requestMatchers("/admin-panel").hasRole("ADMIN"))
                .httpBasic(withDefaults())
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
        config.setAllowedHeaders(singletonList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        return config;
    }
}
