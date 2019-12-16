package ru.itis.chat.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginPasswordDto {
    private String login;
    private String password;
    private boolean jwt;
}
