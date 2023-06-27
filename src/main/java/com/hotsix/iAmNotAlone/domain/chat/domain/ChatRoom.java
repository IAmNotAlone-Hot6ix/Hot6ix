package com.hotsix.iAmNotAlone.domain.chat.domain;

import com.hotsix.iAmNotAlone.domain.chat.dto.AddChatRoomForm;
import com.hotsix.iAmNotAlone.domain.common.BaseEntity;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Membership sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Membership receiver;

//    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
//    private List<ChatMessage> chatMessageList = new ArrayList<>();

    public static ChatRoom from(Membership sender, Membership receiver) {
        return ChatRoom.builder()
            .sender(sender)
            .receiver(receiver)
//            .chatMessageList(new ArrayList<>())
            .build();
    }
//    public void updateLastMessage(String message) {
//        this.lastMessage = message;
//    }

}
