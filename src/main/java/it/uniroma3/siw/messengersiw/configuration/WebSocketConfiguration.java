package it.uniroma3.siw.messengersiw.configuration;

import it.uniroma3.siw.messengersiw.security.websocket.WebSocketAuthInterceptor;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;


@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
@AllArgsConstructor
@Order(HIGHEST_PRECEDENCE + 50)
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    public static final String CHAT_ENDPOINT = "siw-messenger";

    @Autowired
    private WebSocketAuthInterceptor authInterceptorAdapter;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(CHAT_ENDPOINT)
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/", "/queue/");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(this.authInterceptorAdapter);
    }

}
