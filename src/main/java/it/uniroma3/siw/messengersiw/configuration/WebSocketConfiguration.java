package it.uniroma3.siw.messengersiw.configuration;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.user.DefaultUserDestinationResolver;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.*;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * @author Mattia Micaloni
 */
@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
@AllArgsConstructor
@Order(HIGHEST_PRECEDENCE + 50)
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    public static final String CHAT_ENDPOINT = "siw-messenger";

    private final DefaultSimpUserRegistry userRegistry = new DefaultSimpUserRegistry();
    private final DefaultUserDestinationResolver resolver = new DefaultUserDestinationResolver(userRegistry);

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