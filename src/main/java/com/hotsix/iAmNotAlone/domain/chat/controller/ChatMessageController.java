package com.hotsix.iAmNotAlone.domain.chat.controller;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_CHATROOM;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatRoom;
import com.hotsix.iAmNotAlone.domain.chat.dto.AddChatMessageFrom;
import com.hotsix.iAmNotAlone.domain.chat.dto.ChatMessage2Dto;
import com.hotsix.iAmNotAlone.domain.chat.repository.ChatRoomRepository;
import com.hotsix.iAmNotAlone.domain.chat.service.ChatMessageService;
import com.hotsix.iAmNotAlone.domain.chat.service.ChatSenderConfrimService;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.MembershipSummaryDto;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
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
    private final ChatSenderConfrimService chatSenderConfrimService;

    @MessageMapping("/chat/send")
    public void sendToMessage(AddChatMessageFrom form) {
        // 여기서 상대방이 구독중인지 확인을 하고 구독중이면 unRead를 0으로 저장

        // 상대방이 구독중하고 있지 않다면 unRead를 1로 저장, 상대방에게 알람메세지 전송
        log.info("connect: " + redisUtil.getData("chatRoomId: " + form.getChatRoomId()));
        if (!redisUtil.getData("chatRoomId: " + form.getChatRoomId()).equals("2")) {
            form.setUnRead(1);

            Membership sender = chatSenderConfrimService.getSender(form.getSenderId());
            ChatMessage2Dto alarmMessage = ChatMessage2Dto.from(form, MembershipSummaryDto.from(sender));
            simpMessagingTemplate.convertAndSend("/sub/membership/" + form.getReceiverId(), alarmMessage);
        }

        // 채팅방으로 메세지 브로드캐스팅
        simpMessagingTemplate.convertAndSend("/sub/room/" + form.getChatRoomId(), form);


        // 채팅 메세지 DB에 저장
        chatMessageService.addMessage(form);

    }
}
