package com.hotsix.iAmNotAlone.domain.chat.dto;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class AddChatMessageFrom {

    private Long chatRoomId;
    private Long senderId;
    private String message;
    private String createdAt;

}
