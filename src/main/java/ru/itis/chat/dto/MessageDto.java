package ru.itis.chat.dto;

import lombok.*;
import ru.itis.chat.models.Message;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    private String token;
    private String text;
    private String username;
    private Long userId;

    public static MessageDto from(Message message) {
        return MessageDto.builder()
                //.token(message.getUser().getToken().getValue())
                .userId(message.getUser().getId())
                .text(message.getText())
                .username(message.getUser().getFirstName())
                .build();
    }

    public static List<MessageDto> from(List<Message> messages) {
        return messages.stream().map(MessageDto::from).collect(Collectors.toList());
    }
}
