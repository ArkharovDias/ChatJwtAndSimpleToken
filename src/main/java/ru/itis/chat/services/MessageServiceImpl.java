package ru.itis.chat.services;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.chat.models.Message;
import ru.itis.chat.models.Token;
import ru.itis.chat.models.User;
import ru.itis.chat.repositories.MessageRepository;
import ru.itis.chat.repositories.TokensRepository;
import ru.itis.chat.repositories.UsersRepository;
import ru.itis.chat.dto.MessageDto;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TokensRepository tokensRepository;

    /*@Override
    public void save(MessageDto messageDto) {

        Optional<Token> token = tokensRepository.findOneByValue(messageDto.getToken());

        if (token.isPresent()){

            Optional<User> user = usersRepository.findOneByToken(token.get().getValue());

            if (user.isPresent()){

                Message message = Message.builder()
                        .text(messageDto.getText())
                        .user(user.get())
                        .date(new Date())
                        .build();

                messageDto.setUsername(user.get().getFirstName());

                messageRepository.save(message);

            }else throw new IllegalArgumentException("User not found!");

        }else throw new IllegalArgumentException("Token not found!");
    }*/

    @Override
    public void save(MessageDto messageDto) {


        if (isJwt(messageDto.getToken())){

            String login = (String) Jwts.parser()
                    .setSigningKey("java")
                    .parseClaimsJws(messageDto.getToken())
                    .getBody()
                    .get("login");

            Optional<User> user = usersRepository.findOneByLogin(login);

            if (user.isPresent()){

                Message message = Message.builder()
                        .text(messageDto.getText())
                        .user(user.get())
                        .date(new Date())
                        .build();

                messageDto.setUsername(user.get().getFirstName());

                messageRepository.save(message);

            }else {
                throw new IllegalArgumentException("User not found!");
            }
        }else {
            Optional<Token> token = tokensRepository.findOneByValue(messageDto.getToken());

            if (token.isPresent()){

                Optional<User> user = usersRepository.findOneByToken(token.get().getValue());

                if (user.isPresent()){

                    Message message = Message.builder()
                            .text(messageDto.getText())
                            .user(user.get())
                            .date(new Date())
                            .build();

                    messageDto.setUsername(user.get().getFirstName());

                    messageRepository.save(message);

                }else throw new IllegalArgumentException("User not found!");

            }else throw new IllegalArgumentException("Token not found!");
        }



    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> findAllSortByDate() {
        return messageRepository.findAllSortByDate();
    }

    private boolean isJwt(String token){
        return token.contains(".");
    }
}
