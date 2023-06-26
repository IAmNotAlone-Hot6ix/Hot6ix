package com.hotsix.iAmNotAlone.domain.chat.repository;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select r from ChatRoom r where r.sender.id = :id or r.receiver.id = :id")
    List<ChatRoom> findByMemberIdOrderByCreatedAtDesc(Long id);
}
