package com.hotsix.iAmNotAlone.domain.chat.repository;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatMessage;
import com.hotsix.iAmNotAlone.domain.chat.domain.ChatRoom;
import java.util.List;
import java.util.Optional;
import javax.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select r from ChatRoom r where r.sender.id = :id or r.receiver.id = :id")
    List<ChatRoom> findByMemberIdOrderByCreatedAtDesc(Long id);

    // 프론트에서 채팅 보내기 클릭시 해당 유저와 채팅방이 존재하는지 판단해야암
    @Query("select r from ChatRoom r where r.sender.id = :senderId and r.receiver.id = :receiverId or r.sender.id = :receiverId and r.receiver.id = :senderId")
    Optional<ChatRoom> findBySenderIdAndReceiverId(Long senderId, Long receiverId);


//    @EntityGraph(attributePaths = {})
//    ChatRoom findById(Long id);
}
