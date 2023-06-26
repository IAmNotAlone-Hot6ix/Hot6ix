package com.hotsix.iAmNotAlone.domain.chat.service;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatRoom;
import com.hotsix.iAmNotAlone.domain.chat.dto.ChatRoomDto;
import com.hotsix.iAmNotAlone.domain.chat.repository.ChatRoomRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomListService {

    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoomDto> getChatRoomList(Long membershipId) {


        List<ChatRoomDto> chatRoomDtoList = new ArrayList<>();
        for (ChatRoom c : chatRoomRepository.findByMemberIdOrderByCreatedAtDesc(membershipId)) {
            if (Objects.equals(c.getSender().getId(), membershipId)) {
                chatRoomDtoList.add(ChatRoomDto.from(c, c.getReceiver()));
            } else {
                chatRoomDtoList.add(ChatRoomDto.from(c, c.getSender()));
            }
        }

        return chatRoomDtoList;
    }

}