package ru.itis.chat.services;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.chat.dto.JwtDto;
import ru.itis.chat.models.Token;
import ru.itis.chat.models.User;
import ru.itis.chat.repositories.TokensRepository;
import ru.itis.chat.security.tokenfactory.TokenFactory;
import ru.itis.chat.security.tokentype.TokenType;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokenServiceImpl implements TokenService{

    @Autowired
    private TokenFactory tokenFactory;

    @Autowired
    private TokensRepository tokensRepository;

    @Override
    public Map<String, String> refreshTokens(User user) {

        Map<String, String> tokenMap = new HashMap<>();

        JwtDto accessToken = tokenFactory.createAccessToken(user);
        JwtDto refreshToken = tokenFactory.createRefreshToken(user);

        tokenMap.put("accessToken", accessToken.getValue());
        tokenMap.put("refreshToken", refreshToken.getValue());

        tokensRepository.updateByUserId(refreshToken.getValue(), user.getId(), TokenType.REFRESH_TOKEN);

        return tokenMap;
    }
}
