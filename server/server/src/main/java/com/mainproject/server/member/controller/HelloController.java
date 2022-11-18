package com.mainproject.server.member.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    private final OAuth2AuthorizedClientService authorizedClientService;

    public HelloController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/token")
    public String home(Authentication authentication) {
        var authorizedClient = authorizedClientService.loadAuthorizedClient("google", authentication.getName());

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        System.out.println("Access Token Value: " + accessToken.getTokenValue());
        System.out.println("Access Token Type: " + accessToken.getTokenType().getValue());
        System.out.println("Access Token Scopes: " + accessToken.getScopes());
        System.out.println("Access Token Issued At: " + accessToken.getIssuedAt());
        System.out.println("Access Token Expires At: " + accessToken.getExpiresAt());

        return "index";
    }
}
