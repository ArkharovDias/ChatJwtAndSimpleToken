package ru.itis.chat.services;

import ru.itis.chat.models.User;

import java.util.Map;

public interface TokenService {
    Map<String, String> refreshTokens(User user);
}
