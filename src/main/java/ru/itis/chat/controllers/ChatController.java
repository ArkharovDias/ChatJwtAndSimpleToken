package ru.itis.chat.controllers;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.chat.models.Message;
import ru.itis.chat.services.MessageService;
import ru.itis.chat.dto.MessageDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    private final Map<String, List<MessageDto>> messages = new HashMap<>();

    @Autowired
    private MessageService messageService;

    @PostMapping("/messages")
    public ResponseEntity<Object> receiveMessage(@RequestBody MessageDto message) {

        if (!messages.containsKey(message.getToken())) {

            List<Message> allMessagesFromDb = messageService.findAllSortByDate();

            messages.put(message.getToken(), new ArrayList<>(MessageDto.from(allMessagesFromDb)));
        }
        for (List<MessageDto> pageMessages : messages.values()) {

            synchronized (pageMessages) {

                if (!message.getText().isEmpty()){
                    messageService.save(message);
                    pageMessages.add(message);
                }

                pageMessages.notifyAll();
            }
        }
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping("/messages")
    //@Transactional
    public ResponseEntity<List<MessageDto>> getMessagesForPage(@RequestParam("token") String token) {
        synchronized (messages.get(token)) {
            if (messages.get(token).isEmpty()) {
                messages.get(token).wait();
            }
            List<MessageDto> response = new ArrayList<>(messages.get(token));
            messages.get(token).clear();
            System.out.println(response);
            return ResponseEntity.ok(response);
        }
    }
}
