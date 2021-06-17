package it.uniroma3.siw.messengersiw.service;

import it.uniroma3.siw.messengersiw.model.Chat;
import it.uniroma3.siw.messengersiw.model.Message;
import it.uniroma3.siw.messengersiw.model.User;
import it.uniroma3.siw.messengersiw.repository.MessageRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;


    public Message addMessageToChat(User sender, Chat chat, String text) {
        Message message = new Message(sender, chat, text);

        return this.messageRepository.save(message);
    }
}
