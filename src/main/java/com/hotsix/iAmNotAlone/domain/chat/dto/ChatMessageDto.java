package com.hotsix.iAmNotAlone.domain.chat.dto;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatMessage;
import java.time.LocalDateTime;
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
    private int unRead;

    public static ChatMessageDto from(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
            .chatRoomId(chatMessage.getChatRoomId())
            .senderId(chatMessage.getSenderId())
            .message(chatMessage.getMessage())
            .createdAt(chatMessage.getCreatedAt())
            .unRead(chatMessage.getUnRead())
            .build();
    }
}
