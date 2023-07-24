package com.hotsix.iAmNotAlone.domain.chat.service;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatRoom;
import com.hotsix.iAmNotAlone.domain.chat.repository.ChatRoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomRemoveService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void removeChatRoom(Long memberId) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findByMemberIdOrderByCreatedAtDesc(
            memberId);
        for (ChatRoom chatRoom : chatRoomList) {
            chatRoomRepository.deleteById(chatRoom.getId());
        }
    }

}
