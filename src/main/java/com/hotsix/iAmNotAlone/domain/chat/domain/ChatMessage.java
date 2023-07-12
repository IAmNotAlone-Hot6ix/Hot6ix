package com.hotsix.iAmNotAlone.domain.chat.domain;

import com.hotsix.iAmNotAlone.domain.chat.dto.AddChatMessageFrom;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @Column(name = "chat_room_id")
    private Long chatRoomId;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String message;

    private int unRead;

    public static ChatMessage of(AddChatMessageFrom form) {

        return ChatMessage.builder()
            .chatRoomId(form.getChatRoomId())
            .senderId(form.getSenderId())
            .message(form.getMessage())
            .createdAt(form.getCreatedAt())
            .unRead(form.getUnRead())
            .build();
    }

    public void readCheck() {
        this.unRead = 0;
    }

}
