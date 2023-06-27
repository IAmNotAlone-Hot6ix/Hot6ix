package com.hotsix.iAmNotAlone.domain.chat.dto;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatMessage;
import com.hotsix.iAmNotAlone.domain.chat.domain.ChatRoom;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageDto {

    private Long chatRoomId;
    private Long senderId;
    private String message;
    private String createdAt;

    public static ChatMessageDto from(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
            .chatRoomId(chatMessage.getChatRoom().getId())
            .senderId(chatMessage.getSender().getId())
            .message(chatMessage.getMessage())
            .createdAt(chatMessage.getCreatedAt())
            .build();
    }
}
