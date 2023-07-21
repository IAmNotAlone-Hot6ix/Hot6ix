package com.hotsix.iAmNotAlone.domain.chat.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddChatMessageFrom {

    private Long chatRoomId;
    private Long senderId;
    private String message;
    private LocalDateTime createdAt;
    private int unRead;

}
