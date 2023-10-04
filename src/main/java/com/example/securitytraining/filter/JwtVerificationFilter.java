package com.example.securitytraining.filter;

import com.example.securitytraining.security.JwtTokenProcessor;
import com.example.securitytraining.security.model.JwtTokenBody;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtTokenProcessor jwtTokenProcessor;
    private final Set<String> ignoreUris;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var jwtToken = request.getHeader(AUTHORIZATION);

        try {
            var jwtTokenBody = jwtTokenProcessor.parseJwtToken(jwtToken);
            var authentication = new UsernamePasswordAuthenticationToken(jwtTokenBody.getUsername(), null, jwtTokenBody.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid jwt token", e);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return ignoreUris.contains(request.getRequestURI()) || super.shouldNotFilter(request);
    }
}
