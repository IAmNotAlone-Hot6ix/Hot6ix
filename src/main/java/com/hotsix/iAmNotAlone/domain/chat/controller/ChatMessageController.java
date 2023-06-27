package com.hotsix.iAmNotAlone.domain.chat.controller;

import com.hotsix.iAmNotAlone.domain.chat.dto.AddChatMessageFrom;
import com.hotsix.iAmNotAlone.domain.chat.service.ChatMessageService;
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

    @MessageMapping("/chat/send")
    public void sendToMessage(AddChatMessageFrom form) {
        chatMessageService.addMessage(form);
        simpMessagingTemplate.convertAndSend(
            "/sub/room/" + form.getChatRoomId(), form);

    }
}
