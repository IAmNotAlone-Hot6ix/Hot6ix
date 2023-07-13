package com.hotsix.iAmNotAlone.domain.chat.dto;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatMessage;
import com.hotsix.iAmNotAlone.domain.chat.domain.ChatRoom;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.MembershipSummaryDto;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class ChatRoomDto {

    private Long chatRoomId;
    private MembershipSummaryDto partner;
    private String lastMessage;
    private LocalDateTime lastTime;
    private Long unRead;

    public static ChatRoomDto from(ChatRoom chatRoom, Membership partner, ChatMessage message) {
        return ChatRoomDto.builder()
            .chatRoomId(chatRoom.getId())
            .partner(MembershipSummaryDto.from(partner))
            .lastMessage(message.getMessage())
            .lastTime(message.getCreatedAt())
            .build();
    }

}
