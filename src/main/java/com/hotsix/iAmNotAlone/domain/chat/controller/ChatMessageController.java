package com.hotsix.iAmNotAlone.domain.chat.controller;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_CHATROOM;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatRoom;
import com.hotsix.iAmNotAlone.domain.chat.dto.AddChatMessageFrom;
import com.hotsix.iAmNotAlone.domain.chat.repository.ChatRoomRepository;
import com.hotsix.iAmNotAlone.domain.chat.service.ChatMessageService;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.util.RedisUtil;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class ChatMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;
    private final RedisUtil redisUtil;
    private final ChatRoomRepository chatRoomRepository;

    @MessageMapping("/chat/send")
    public void sendToMessage(AddChatMessageFrom form) {
        // 여기서 상대방이 구독중인지 확인을 하고 구독중이면 unRead를 0으로 저장


        // 상대방이 구독중하고 있지 않다면 unRead를 1로 저장
        log.info("connect: " + redisUtil.getData(String.valueOf(form.getChatRoomId())));

        simpMessagingTemplate.convertAndSend("/sub/room/" + form.getChatRoomId(), form);

        ChatRoom chatRoom = chatRoomRepository.findById(form.getChatRoomId()).orElseThrow(
            () -> new BusinessException(NOT_FOUND_CHATROOM)
        );

        Long receiverId;
        if (Objects.equals(chatRoom.getSender().getId(), form.getSenderId())) {
            receiverId = chatRoom.getReceiver().getId();
        } else {
            receiverId = chatRoom.getSender().getId();
        }

        simpMessagingTemplate.convertAndSend("/sub/membership/" + receiverId, form);
        chatMessageService.addMessage(form);

    }
}
