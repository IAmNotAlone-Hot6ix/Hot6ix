package com.hotsix.iAmNotAlone.domain.chat.config;

import static com.hotsix.iAmNotAlone.global.auth.jwt.JwtProperties.TOKEN_PREFIX;

import com.hotsix.iAmNotAlone.domain.chat.service.ChatMessageService;
import com.hotsix.iAmNotAlone.global.auth.jwt.JwtService;
import com.hotsix.iAmNotAlone.global.util.RedisUtil;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class FilterChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final RedisUtil redisUtil;
    private final ChatMessageService chatMessageService;


    /**
     * 연결, SEND 시 선조취 하는 메서드
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(stompHeaderAccessor.getCommand())
                ) {
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
                    throw new AccessDeniedException("유효한 접근이 아닙니다. 다시 접속해주세요.");
                }
            } else {
                log.info("토큰 없음");
                throw new AuthenticationException("서버 연결에 실패했습니다. 다시 접속해주세요.") {};
            }
        }

        return message;
    }


    /**
     * CONNECT, DISCONNECT 한 후 후조취 하는 메서드
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
            case SUBSCRIBE:
                log.info("SUBSCRIBE");
                log.info("sessionId: {}", sessionId);
                String sRoomId = accessor.getDestination().split("/")[3];

                redisUtil.setData(sessionId, sRoomId);
                redisUtil.addConnect("chatRoomId: " + sRoomId);
                log.info("채팅방에 들어와있는 유저수: " + redisUtil.getData("chatRoomId: " + sRoomId));

                String token = accessor.getFirstNativeHeader("Authorization");
                log.info("token: {}", token);
                if (token != null && token.startsWith(TOKEN_PREFIX)) {
                    String jwtToken = token.replace(TOKEN_PREFIX, "");

                    Long membershipId = jwtService.extractUserId(jwtToken).get();
                    Long roomId = Long.parseLong(sRoomId);

                    Long unReadCount = chatMessageService.unReadCount(roomId, membershipId);
                    log.info(unReadCount);
                    if (unReadCount != 0) {
                        chatMessageService.readCheck(roomId, membershipId);
                    }
                }
                break;
            case UNSUBSCRIBE:
                log.info("UNSUBSCRIBE");
                log.info("sessionId: {}", sessionId);
                String dRoomId = redisUtil.getData(sessionId);
                redisUtil.deleteConnect("chatRoomId: " + dRoomId);
                redisUtil.deleteData(sessionId);
                log.info("채팅방에 들어와있는 유저수: " + redisUtil.getData("chatRoomId: " + dRoomId));

            case DISCONNECT:
                // 웹소켓 클라이언트가 disconnect()를 호출하여 연결을 종료한 경우 또는 세션이 끊어진 경우
                log.info("DISCONNECT");
                log.info("sessionId: {}", sessionId);

                break;
            case SEND:
                log.info("SEND");
                log.info("sessionId: " + sessionId);
                String sendRoomId = redisUtil.getData(sessionId);
                log.info("채팅방에 들어와있는 유저수: " + redisUtil.getData("chatRoomId: " + sendRoomId));
                break;
            default:
                break;
        }
    }


}
