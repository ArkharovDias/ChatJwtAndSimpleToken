package ru.itis.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.chat.models.Token;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String userLogin;
    private String value;
    private String expiredDate;

    public static TokenDto from(Token token) {
        return TokenDto.builder()
                .value(token.getValue())
                .build();
    }


}
