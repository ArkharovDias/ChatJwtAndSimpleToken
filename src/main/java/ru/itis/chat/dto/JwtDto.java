package ru.itis.chat.dto;

import io.jsonwebtoken.Claims;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtDto {
    private Claims claims;
    private String value;
}
