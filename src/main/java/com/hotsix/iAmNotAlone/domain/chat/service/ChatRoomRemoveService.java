package com.hotsix.iAmNotAlone.domain.chat.service;

import com.hotsix.iAmNotAlone.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomRemoveService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public Boolean removeChatRoom(Long chatRoomId) {
        return false;
    }

}
