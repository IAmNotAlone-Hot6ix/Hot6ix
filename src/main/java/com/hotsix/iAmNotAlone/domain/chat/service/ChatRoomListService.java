package com.hotsix.iAmNotAlone.domain.chat.service;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatRoom;
import com.hotsix.iAmNotAlone.domain.chat.dto.ChatRoomDto;
import com.hotsix.iAmNotAlone.domain.chat.repository.ChatMessageRepository;
import com.hotsix.iAmNotAlone.domain.chat.repository.ChatRoomRepository;
import java.util.ArrayList;
import java.util.Comparator;
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
    private final ChatMessageRepository chatMessageRepository;

    public List<ChatRoomDto> getChatRoomList(Long membershipId) {

        List<ChatRoomDto> chatRoomDtoList = new ArrayList<>();
        for (ChatRoom c : chatRoomRepository.findByMemberIdOrderByCreatedAtDesc(membershipId)) {

            if (Objects.equals(c.getSender().getId(), membershipId)) {

                chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(c.getId())
                    .ifPresent(chatMessage -> chatRoomDtoList.add(
                        ChatRoomDto.from(c, c.getReceiver(), chatMessage)));

            } else {
                chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(c.getId())
                    .ifPresent(chatMessage -> chatRoomDtoList.add(
                        ChatRoomDto.from(c, c.getSender(), chatMessage)));
            }
        }

        for (ChatRoomDto chatRoomDto : chatRoomDtoList) {
            Long unRead = chatMessageRepository.countUnReadMessage(chatRoomDto.getChatRoomId(), membershipId);
            chatRoomDto.setUnRead(unRead);
        }
        return chatRoomDtoList.stream()
            .sorted(Comparator.comparing(ChatRoomDto::getLastTime).reversed()).collect(
                Collectors.toList());
    }

}