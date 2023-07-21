package com.hotsix.iAmNotAlone.domain.chat.dto;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatMessage;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.MembershipSummaryDto;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class ChatMessage2Dto {

    private Long chatRoomId;
    private MembershipSummaryDto membership;
    private String message;
    private LocalDateTime createdAt;
    private int unRead;

    public static ChatMessage2Dto from(AddChatMessageFrom chatMessage, MembershipSummaryDto membership) {
        return ChatMessage2Dto.builder()
            .chatRoomId(chatMessage.getChatRoomId())
            .membership(membership)
            .message(chatMessage.getMessage())
            .createdAt(chatMessage.getCreatedAt())
            .unRead(chatMessage.getUnRead())
            .build();
    }

}
