package ru.itis.chat.services;

import ru.itis.chat.models.Message;
import ru.itis.chat.dto.MessageDto;

import java.util.List;

public interface MessageService {
    void save(MessageDto messageDto);
    List<Message> findAll();
    List<Message> findAllSortByDate();
}
