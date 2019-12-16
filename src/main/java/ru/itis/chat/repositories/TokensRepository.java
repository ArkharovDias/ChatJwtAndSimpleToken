package ru.itis.chat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.itis.chat.models.Token;
import ru.itis.chat.security.role.Role;
import ru.itis.chat.security.tokentype.TokenType;

import java.util.Date;
import java.util.Optional;


public interface TokensRepository extends JpaRepository<Token, Long> {
    @Query("from Token token where token.value = ?1")
    Optional<Token> findOneByValue(String value);

    @Query("from Token token where user.id = ?1")
    Optional<Token> findOneByUserId(Long id);

    @Modifying
    @Query("update Token token set token.value = ?1 where token.user.id = ?2 and token.tokenType = ?3")
    void updateByUserId(String value, Long userId, TokenType tokenType);

}
