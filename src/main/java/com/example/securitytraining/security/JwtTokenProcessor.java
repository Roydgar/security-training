package com.example.securitytraining.security;

import com.example.securitytraining.security.model.JwtTokenBody;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.example.securitytraining.security.SecurityConstants.JWT_TOKEN_SECRET;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.StringUtils.commaDelimitedListToSet;

@Profile("auth.jwt")
@Component
@RequiredArgsConstructor
public class JwtTokenProcessor {
    private static final SecretKey JWT_SIGNATURE_KEY = Keys.hmacShaKeyFor(JWT_TOKEN_SECRET.getBytes(UTF_8));


    public String createJwtToken(UserDetails userDetails) {
        var now = new Date();

        return Jwts.builder()
                .setIssuer("Roydgar")
                .setSubject("Security Training")
                .claim("username", userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 300000))
                .signWith(JWT_SIGNATURE_KEY)
                .compact();
    }

    public JwtTokenBody parseJwtToken(String bearerToken) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(JWT_SIGNATURE_KEY)
                .build()
                .parseClaimsJws(bearerToken)
                .getBody();

        return JwtTokenBody.builder()
                .username(String.valueOf(claims.get("username")))
                .authorities(toGrantedAuthorities(commaDelimitedListToSet(String.valueOf(claims.get("authorities")))))
                .build();
    }

    private List<SimpleGrantedAuthority> toGrantedAuthorities(Set<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
