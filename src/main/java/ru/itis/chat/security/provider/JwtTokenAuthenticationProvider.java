package ru.itis.chat.security.provider;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.itis.chat.security.authentication.JwtTokenAuthentication;
import ru.itis.chat.security.details.UserDetailsImpl;

@Component
public class JwtTokenAuthenticationProvider implements AuthenticationProvider {

    @Value("${jwt.secret.value}")
    private String secretKey;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        JwtTokenAuthentication jwtTokenAuthentication = (JwtTokenAuthentication) authentication;

        Claims body;

        try {
            body = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(authentication.getName())
                    .getBody();
        }catch (IllegalArgumentException|MalformedJwtException| SignatureException| ExpiredJwtException e){
            e.printStackTrace();
            throw new AuthenticationServiceException("Invalid token");
        }

        UserDetails userDetails = new UserDetailsImpl(
                Long.parseLong(String.valueOf(body.get("sub"))),
                String.valueOf(body.get("login")),
                String.valueOf(body.get("role"))
                );

        jwtTokenAuthentication.setUserDetails(userDetails);
        jwtTokenAuthentication.setAuthenticated(true);

        return jwtTokenAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtTokenAuthentication.class.equals(authentication);
    }
}
