package it.uniroma3.siw.messengersiw.service;

import it.uniroma3.siw.messengersiw.model.Chat;
import it.uniroma3.siw.messengersiw.model.Message;
import it.uniroma3.siw.messengersiw.model.PrivateChat;
import it.uniroma3.siw.messengersiw.model.User;
import it.uniroma3.siw.messengersiw.repository.ChatRepository;
import it.uniroma3.siw.messengersiw.repository.MessageRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author Mattia Micaloni
 */
@AllArgsConstructor
@Transactional
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;


    public boolean privateChatExists(String username1, String username2) {
        return this.getPrivateChatFromUsernames(username1, username2) != null;
    }

    public Chat getChat(Long id) {
        return this.chatRepository.findById(id)
                .orElse(null);
    }

    public Chat getPrivateChatFromUsernames(String username1, String username2) {
        return this.chatRepository.findByName(PrivateChat.nameFromUsernames(username1, username2))
                .orElse(null);
    }

    public List<Chat> getUserChats(User user) {
        return this.chatRepository.findAllByMembers(user);
    }

    public void addMessageToChat(Chat chat, Message message) {
        if (chat == null || message == null) return;

        this.messageRepository.save(message);
    }

    public Chat save(Chat chat) {
        return this.chatRepository.save(chat);
    }

}
