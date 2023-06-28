package com.hotsix.iAmNotAlone.domain.chat.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AddChatMessageFrom {

    private Long chatRoomId;
    private Long senderId;
    private String message;
    private LocalDateTime createdAt;

}
