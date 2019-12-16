package ru.itis.chat.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.chat.dto.LoginPasswordDto;
import ru.itis.chat.models.User;
import ru.itis.chat.security.details.UserDetailsImpl;
import ru.itis.chat.services.LoginService;
import ru.itis.chat.services.TokenService;


import java.security.Security;
import java.util.Map;


@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/refresh")
    public ResponseEntity<String> refresh(Authentication authentication) {
        //UserDetails userDetails = (UserDetails) authentication.getDetails();
        //UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        //User user = userDetails.getUser();

        System.out.println(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

        return ResponseEntity.ok("");//ResponseEntity.ok(tokenService.refreshTokens(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginPasswordDto loginPasswordDto) {
        System.out.println(loginPasswordDto);
        return ResponseEntity.ok(loginService.login(loginPasswordDto));
    }


}
