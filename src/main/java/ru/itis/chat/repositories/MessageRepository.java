package ru.itis.chat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.chat.models.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("FROM Message message ORDER BY message.date ASC")
    List<Message> findAllSortByDate();
}
