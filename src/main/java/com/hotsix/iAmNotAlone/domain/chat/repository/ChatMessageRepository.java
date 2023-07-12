package com.hotsix.iAmNotAlone.domain.chat.repository;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatMessage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 채팅방에서 채팅 내용 가져오기
    List<ChatMessage> findByChatRoomIdOrderByCreatedAt(Long id);

    // 채팅방 리스트에서 마지막 채팅 기록 가져오기
    Optional<ChatMessage> findFirstByChatRoomIdOrderByCreatedAtDesc(Long id);

    @Query("SELECT COUNT(cm) FROM ChatMessage cm WHERE cm.chatRoomId = :roomId and cm.senderId <> :membershipId and cm.unRead = 1")
    Long countUnReadMessage(Long roomId, Long membershipId);

    @Query("SELECT cm FROM ChatMessage  cm WHERE cm.chatRoomId = :roomId and cm.senderId <> :membershipId and cm.unRead = 1")
    List<ChatMessage> getUnReadMessage(Long roomId, Long membershipId);

}
