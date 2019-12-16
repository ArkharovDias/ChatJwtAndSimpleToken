package ru.itis.chat.services;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.chat.dto.JwtDto;
import ru.itis.chat.dto.RegistrationDto;
import ru.itis.chat.models.Token;
import ru.itis.chat.repositories.TokensRepository;
import ru.itis.chat.security.role.Role;
import ru.itis.chat.security.state.State;
import ru.itis.chat.models.User;
import ru.itis.chat.repositories.UsersRepository;
import ru.itis.chat.security.tokenfactory.TokenFactory;
import ru.itis.chat.security.tokentype.TokenType;

import java.util.*;


@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private TokenFactory tokenFactory;

    @Autowired
    private TokensRepository tokensRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> signUp(RegistrationDto registrationDto) {

        Map<String, String> tokenMap = new HashMap<>();

        String hashPassword = passwordEncoder.encode(registrationDto.getPassword());

        User user = User.builder()
                .firstName(registrationDto.getName())
                .lastName(registrationDto.getSurname())
                .hashPassword(hashPassword)
                .login(registrationDto.getLogin())
                .role(Role.USER)
                .state(State.ACTIVE)
                .build();

        usersRepository.save(user);

        if (registrationDto.isJwt()){

            JwtDto accessToken = tokenFactory.createAccessToken(user);
            JwtDto refreshToken = tokenFactory.createRefreshToken(user);

            tokenMap.put("accessToken", accessToken.getValue());
            tokenMap.put("refreshToken", refreshToken.getValue());

            Token refreshTokenModel = Token.builder()
                    .user(user)
                    .tokenType(TokenType.REFRESH_TOKEN)
                    .value(refreshToken.getValue())
                    .build();

            tokensRepository.save(refreshTokenModel);

        }else {

            Token token = Token.builder()
                    .user(user)
                    .tokenType(TokenType.SIMPLE)
                    .value(UUID.randomUUID().toString())
                    .build();

            tokenMap.put("simpleToken", token.getValue());

            tokensRepository.save(token);
        }

        return tokenMap;
    }

    @Override
    public List<User> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public User findOne(Long userId) {
        return usersRepository.findOne(userId);
    }
}
