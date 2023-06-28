package com.hotsix.iAmNotAlone.domain.chat.repository;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


    List<ChatMessage> findByChatRoomIdOrderByCreatedAt(Long id);

}
