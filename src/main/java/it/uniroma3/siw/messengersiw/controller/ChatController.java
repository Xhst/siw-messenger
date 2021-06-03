package it.uniroma3.siw.messengersiw.controller;

import it.uniroma3.siw.messengersiw.message.incoming.AddGroupChatDto;
import it.uniroma3.siw.messengersiw.message.incoming.AddPrivateChatDto;
import it.uniroma3.siw.messengersiw.message.incoming.ChatMessageDto;
import it.uniroma3.siw.messengersiw.message.outgoing.ChatDto;
import it.uniroma3.siw.messengersiw.message.outgoing.MessageDeliveryDto;
import it.uniroma3.siw.messengersiw.model.Chat;
import it.uniroma3.siw.messengersiw.model.GroupChat;
import it.uniroma3.siw.messengersiw.model.Message;
import it.uniroma3.siw.messengersiw.model.PrivateChat;
import it.uniroma3.siw.messengersiw.model.User;
import it.uniroma3.siw.messengersiw.service.UserService;
import it.uniroma3.siw.messengersiw.service.ChatService;

import lombok.AllArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Web socket controller for chat.
 * Contains message-handling methods mapped with messages by matching the declared patterns to a destination
 * extracted from the message.
 *
 * @author Mattia Micaloni
 */
@AllArgsConstructor
@Controller
public class ChatController {

    private final SimpMessagingTemplate messaging;

    private final ChatService chatService;
    private final UserService userService;


    /**
     * Add a private chat between the principal user and the user sent with the DTO then send response to both clients.
     * If the chat already exists, it does nothing.
     *
     * @param request data transfer object.
     * @param principal It reflects the authenticated user.
     */
    @MessageMapping("/add_private_chat")
    public void addPrivateChat(AddPrivateChatDto request, Principal principal) throws Exception {
        if (principal == null) return;

        User user1 = this.userService.getUser(principal.getName());
        User user2 = this.userService.getUser(request.getUsername().trim());

        if (user2 == null || this.chatService.privateChatExists(user1.getUsername(), user2.getUsername())) return;

        Chat chat = this.chatService.save(new PrivateChat(user1, user2));

        messaging.convertAndSendToUser(user1.getUsername(), "/queue/add_chat",
                new ChatDto(chat.getId(), user2.getUsername(), false, ""));

        messaging.convertAndSendToUser(user2.getUsername(), "/queue/add_chat",
                new ChatDto(chat.getId(), user1.getUsername(), false, ""));
    }

    /**
     * Add a group chat with the users contained in the DTO as members and the principal user as owner,
     * then send the response to each user.
     *
     * @param request data transfer object.
     * @param principal It reflects the authenticated user.
     */
    @MessageMapping("/add_group_chat")
    public void addGroupChat(AddGroupChatDto request, Principal principal) throws Exception {
        if (principal == null || request.getUsernames().length == 0
             || request.getGroupName().trim().isEmpty()) return;

        User owner = this.userService.getUser(principal.getName());

        Set<User> groupChatMembers = new HashSet<>();

        for (String username : request.getUsernames()) {
            User user = this.userService.getUser(username.trim());

            if (user == null) continue;

            groupChatMembers.add(user);
        }

        Chat chat = this.chatService.save(
                new GroupChat(request.getGroupName(), owner, groupChatMembers));

        for (User user : groupChatMembers) {
            messaging.convertAndSendToUser(user.getUsername(), "/queue/add_chat",
                    new ChatDto(chat.getId(), request.getGroupName(), true, owner.getUsername()));
        }
    }

    /**
     * Add message sent by the principal user to the chat in the DTO,
     * then send response to all chat members (except the principal user).
     *
     * @param request data transfer object.
     * @param principal It reflects the authenticated user.
     */
    @MessageMapping("/chat_message")
    public void message(ChatMessageDto request, Principal principal) throws Exception {
        if (principal == null) return;

        User sender = this.userService.getUser(principal.getName());
        Chat recipient = this.chatService.getChat(request.getToChat());

        Message message = new Message(sender, recipient, request.getMessage());

        this.chatService.addMessageToChat(recipient, message);

        for (User user : recipient.getMembers()) {
            if (user.getUsername().equals(sender.getUsername())) continue;

            messaging.convertAndSendToUser(user.getUsername(), "/queue/message",
                    new MessageDeliveryDto(
                            recipient.getId(),
                            sender.getUsername(),
                            message.getText(),
                            message.getSendDate()
            ));

        }
    }

    /**
     * Gets all chats of the principal user and sends them to him.
     *
     * @param principal It reflects the authenticated user.
     * @return list of chat DTOs
     */
    @MessageMapping("/get_chats")
    @SendToUser("queue/chats")
    public List<ChatDto> getChats(Principal principal) {

        User user = this.userService.getUser(principal.getName());
        List<ChatDto> chats = new ArrayList<>();

        for (Chat chat : this.chatService.getUserChats(user)) {
            if (chat instanceof PrivateChat) {
                String chatName = chat.getName()
                        .replace("-", "")
                        .replace(user.getUsername(), "");

                chats.add(new ChatDto(chat.getId(), chatName, false, ""));
            }

            if (chat instanceof GroupChat) {
                chats.add(new ChatDto(chat.getId(), chat.getName(), true, ((GroupChat) chat).getOwner().getUsername()));
            }
        }
        return chats;
    }
}