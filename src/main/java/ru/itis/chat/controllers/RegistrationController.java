package ru.itis.chat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.chat.dto.RegistrationDto;
import ru.itis.chat.services.UsersService;

import java.util.Map;

@RestController
public class RegistrationController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/signUp")
    public ResponseEntity<Map<String, String>> signUp(@RequestBody RegistrationDto registrationDto) {
        return ResponseEntity.ok(usersService.signUp(registrationDto));
    }
}
