package com.example.securitytraining.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Profile("auth.oAuth2")
@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @GetMapping
    public String helloWorld(@AuthenticationPrincipal OAuth2User principal) {
        log.info("oAuth2 protected user: {}", principal.getName());
        return "oauth2-secured.html";
    }
}
