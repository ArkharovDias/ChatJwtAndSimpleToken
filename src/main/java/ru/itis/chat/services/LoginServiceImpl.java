package ru.itis.chat.services;

import io.jsonwebtoken.Claims;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.chat.dto.JwtDto;
import ru.itis.chat.dto.LoginPasswordDto;
import ru.itis.chat.models.Token;
import ru.itis.chat.models.User;
import ru.itis.chat.repositories.TokensRepository;
import ru.itis.chat.repositories.UsersRepository;
import ru.itis.chat.security.role.Role;
import ru.itis.chat.security.tokenfactory.TokenFactory;
import ru.itis.chat.security.tokentype.TokenType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Component
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TokensRepository tokensRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenFactory tokenFactory;

    @Transactional
    @Override
    public Map<String, String> login(LoginPasswordDto loginPasswordDto) {

        Map<String, String> tokenMap = new HashMap<>();

        Optional<User> userCandidate = usersRepository.findOneByLogin(loginPasswordDto.getLogin());

        if (userCandidate.isPresent()){

            User user = userCandidate.get();

            if (passwordEncoder.matches(loginPasswordDto.getPassword(), user.getHashPassword())){

                if (loginPasswordDto.isJwt()){
                    JwtDto accessToken = tokenFactory.createAccessToken(user);
                    JwtDto refreshToken = tokenFactory.createRefreshToken(user);

                    tokenMap.put("accessToken", accessToken.getValue());
                    tokenMap.put("refreshToken", refreshToken.getValue());

                    Claims claims = refreshToken.getClaims();

                    Token refreshTokenModel = Token.builder()
                            .user(user)
                            .tokenType(TokenType.REFRESH_TOKEN)
                            .value(refreshToken.getValue())
                            .build();

                    Optional<Token> userRefreshToken = user.getTokens().stream()
                            .filter(n->n.getTokenType().equals(TokenType.REFRESH_TOKEN))
                            .findAny();

                    if (!userRefreshToken.isPresent()){
                        tokensRepository.save(refreshTokenModel);
                    }else {
                        tokensRepository.updateByUserId(refreshToken.getValue(), user.getId(), TokenType.REFRESH_TOKEN);
                    }
                }else {

                    Optional<Token> userSimpleToken = user.getTokens().stream()
                            .filter(n->n.getTokenType().equals(TokenType.SIMPLE))
                            .findAny();

                    if (!userSimpleToken.isPresent()){

                        Token token = Token.builder()
                                .user(user)
                                .tokenType(TokenType.SIMPLE)
                                .value(UUID.randomUUID().toString())
                                .build();

                        tokensRepository.save(token);

                        tokenMap.put("simpleToken", token.getValue());

                    }else{

                        tokenMap.put("simpleToken", userSimpleToken.get().getValue());
                    }

                }

            }else {
                throw new IllegalArgumentException("Incorrect password!");
            }
        }else {
            throw new IllegalArgumentException("User not Found!");
        }

        return tokenMap;
    }

    /*@Override
    public TokenDto login(LoginForm loginForm) {
        Optional<User> userCandidate = usersRepository.findOneByLogin(loginForm.getLogin());

        if (userCandidate.isPresent()) {
            User user = userCandidate.get();

            if (passwordEncoder.matches(loginForm.getPassword(), user.getHashPassword())) {

                //Optional<Token> token = tokensRepository.findOneByValue();

                if (user.getToken() == null){

                    Token token = Token.builder()
                            .user(user)
                            .value(RandomStringUtils.random(10, true, true))
                            .build();

                    tokensRepository.save(token);

                    return TokenDto.from(token);

                }else{
                    return TokenDto.from(tokensRepository.findOneByUserId(user.getId()).get());
                }


            }
        } throw new IllegalArgumentException("User not found");
    }*/

}
