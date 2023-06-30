package com.hotsix.iAmNotAlone.domain.chat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Log4j2
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final FilterChannelInterceptor filterChannelInterceptor;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS().setHeartbeatTime(10000); // 10초
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메세지 발행 요청 url -> 메세지 보낼 때
        registry.setApplicationDestinationPrefixes("/pub");
        // 메세지 구독 요청 url -> 메세지 받을 때
        registry.enableSimpleBroker("/sub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(filterChannelInterceptor);
    }
}
