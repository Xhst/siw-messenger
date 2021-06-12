package it.uniroma3.siw.messengersiw.configuration;

import it.uniroma3.siw.messengersiw.security.jwt.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author Mattia Micaloni
 */
@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) throws AuthenticationException {

        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;

        StompCommand cmd = accessor.getCommand();

        if (StompCommand.CONNECT == cmd || StompCommand.SEND == cmd) {
            String token = accessor.getFirstNativeHeader("Authorization");

            boolean isTokenValid = this.jwtUtils.isTokenValid(token);
            String username = this.jwtUtils.getUsernameFromToken(token);

            if (!isTokenValid || username == null) return message;

            accessor.setUser(() -> username);
        }
        return message;
    }
}