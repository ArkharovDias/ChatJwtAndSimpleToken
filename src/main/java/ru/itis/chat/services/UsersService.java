package ru.itis.chat.services;

import ru.itis.chat.dto.RegistrationDto;
import ru.itis.chat.models.User;

import java.util.List;
import java.util.Map;


public interface UsersService {
    Map<String, String> signUp(RegistrationDto registrationDto);

    List<User> findAll();

    User findOne(Long userId);
}
