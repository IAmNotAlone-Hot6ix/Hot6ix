package com.hotsix.iAmNotAlone.domain.chat.domain;

import com.hotsix.iAmNotAlone.domain.chat.dto.AddChatMessageFrom;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Membership sender;

    private String createdAt;

    private String message;

    public static ChatMessage of(AddChatMessageFrom form) {

        return ChatMessage.builder()
            .chatRoom(ChatRoom.builder().id(form.getChatRoomId()).build())
            .sender(Membership.builder().id(form.getSenderId()).build())
            .message(form.getMessage())
            .createdAt(form.getCreatedAt())
            .build();
    }
}
