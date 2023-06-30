package com.hotsix.iAmNotAlone.domain.chat.config;

import static com.hotsix.iAmNotAlone.global.auth.jwt.JwtProperties.TOKEN_PREFIX;

import com.hotsix.iAmNotAlone.global.auth.jwt.JwtService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
@RequiredArgsConstructor
@Log4j2
public class FilterChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;


    /**
     * 연결 시
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(stompHeaderAccessor.getCommand())) {
            log.info("StompCommand.CONNECT");
            
            // Authorization 의 값 중 첫번째 값
            String token = stompHeaderAccessor.getFirstNativeHeader("Authorization");
            log.debug(token);

            if (token != null && token.startsWith(TOKEN_PREFIX)) {
                String jwtToken = token.replace(TOKEN_PREFIX, "");

                if (jwtService.isTokenValid(jwtToken)) {
                    log.info("유효성 검사 성공");

                } else {
                    log.info("유효성 검사 실패");
                    throw new AuthenticationException("서버 연결에 실패했습니다. 다시 접속해주세요.") {};
                }
            } else {
                log.info("토큰 없음");
                throw new AuthenticationException("서버 연결에 실패했습니다. 다시 접속해주세요.") {};
            }
        }

        return message;
    }


    /**
     *
     */
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();

        switch (Objects.requireNonNull(accessor.getCommand())) {
            case CONNECT:
                // 웹소켓 클라이언트가 connect()를 호출하여 연결한 경우
                log.info("CONNECT");
                log.info("sessionId: {}", sessionId);

                break;
            case DISCONNECT:
                // 웹소켓 클라이언트가 disconnect()를 호출하여 연결을 종료한 경우 또는 세션이 끊어진 경우
                log.info("DISCONNECT");
                log.info("sessionId: {}", sessionId);

                break;
            default:
                break;
        }
    }


}
