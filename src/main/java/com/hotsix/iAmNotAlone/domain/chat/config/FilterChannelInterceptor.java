package com.hotsix.iAmNotAlone.domain.chat.config;

import static com.hotsix.iAmNotAlone.global.auth.jwt.JwtProperties.TOKEN_PREFIX;

import com.hotsix.iAmNotAlone.global.auth.jwt.JwtService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
@RequiredArgsConstructor
@Log4j2
public class FilterChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(stompHeaderAccessor.getCommand()) || StompCommand.SEND.equals(stompHeaderAccessor.getCommand())) {
            List<String> nativeHeader = stompHeaderAccessor.getNativeHeader("Authorization");

            if (nativeHeader != null && !nativeHeader.isEmpty()) {
                String token = nativeHeader.get(0);

                if (token.startsWith(TOKEN_PREFIX)) {
                    String jwtToken = token.replace(TOKEN_PREFIX, "");

                    if (jwtService.isTokenValid(jwtToken)) {
                        log.info("유효성 검사 성공");

                    } else {
                        log.info("유효성 검사 성공");
                        throw new MessageDeliveryException("유효하지 않은 접근입니다.");
                    }
                }
            } else {
                throw new MessageDeliveryException("유효하지 않은 접근입니다.");
            }
        }
        return message;
    }
}
