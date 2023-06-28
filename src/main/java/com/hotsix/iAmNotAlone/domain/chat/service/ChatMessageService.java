package com.hotsix.iAmNotAlone.domain.chat.service;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatMessage;
import com.hotsix.iAmNotAlone.domain.chat.dto.AddChatMessageFrom;
import com.hotsix.iAmNotAlone.domain.chat.dto.ChatMessageDto;
import com.hotsix.iAmNotAlone.domain.chat.repository.ChatMessageRepository;
import com.hotsix.iAmNotAlone.domain.chat.repository.ChatRoomRepository;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MembershipRepository membershipRepository;

    @Transactional
    public Long addMessage(AddChatMessageFrom form) {
//        ChatRoom chatRoom = chatRoomRepository.findById(form.getChatRoomId()).orElseThrow(
//            () -> new BusinessException(ErrorCode.NOT_FOUND_CHATROOM)
//        );
//
//        Membership sender = membershipRepository.findById(form.getSenderId()).orElseThrow(
//            () -> new BusinessException(ErrorCode.NOT_FOUND_USER)
//        );

        ChatMessage chatMessage = chatMessageRepository.save(
            ChatMessage.of(form));
        return chatMessage.getId();
    }

    public List<ChatMessageDto> getMessageList(Long roomId) {
//        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
//            () -> new BusinessException(ErrorCode.NOT_FOUND_CHATROOM)
//        );
        return chatMessageRepository.findByChatRoomIdOrderByCreatedAtDesc(roomId).stream().map(
            ChatMessageDto::from).collect(Collectors.toList());
    }
}
