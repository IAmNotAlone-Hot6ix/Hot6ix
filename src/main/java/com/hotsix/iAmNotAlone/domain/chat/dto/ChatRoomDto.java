package com.hotsix.iAmNotAlone.domain.chat.dto;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatRoom;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.MembershipSummaryDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomDto {

    private Long chatRoomId;
    private MembershipSummaryDto partner;
    private String lastMessage;

    public static ChatRoomDto from(ChatRoom chatRoom, Membership partner) {
        return ChatRoomDto.builder()
            .chatRoomId(chatRoom.getId())
            .partner(MembershipSummaryDto.from(partner))
            .build();
    }
}
