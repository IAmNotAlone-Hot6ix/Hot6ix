package com.hotsix.iAmNotAlone.domain.chat.dto;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatMessage;
import com.hotsix.iAmNotAlone.domain.chat.domain.ChatRoom;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class ChatMessageDto {

    private Long chatRoomId;
    private Long senderId;
    private String message;
    private LocalDateTime createdAt;

    public static ChatMessageDto from(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
            .chatRoomId(chatMessage.getChatRoomId())
            .senderId(chatMessage.getSenderId())
            .message(chatMessage.getMessage())
            .createdAt(chatMessage.getCreatedAt())
            .build();
    }
}
