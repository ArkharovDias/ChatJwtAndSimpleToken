package ru.itis.chat.services;

import ru.itis.chat.dto.LoginPasswordDto;

import java.util.Map;


public interface LoginService {
    Map<String, String> login(LoginPasswordDto loginPasswordDto);
}
