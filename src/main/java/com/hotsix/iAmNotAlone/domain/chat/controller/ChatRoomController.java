package com.hotsix.iAmNotAlone.domain.chat.controller;

import com.hotsix.iAmNotAlone.domain.chat.dto.AddChatRoomForm;
import com.hotsix.iAmNotAlone.domain.chat.dto.ChatRoomDto;
import com.hotsix.iAmNotAlone.domain.chat.service.ChatRoomListService;
import com.hotsix.iAmNotAlone.domain.chat.service.ChatRoomRegisterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomRegisterService chatRoomRegisterService;
    private final ChatRoomListService chatRoomListService;

    @PostMapping("/api/chat/room")
    public ResponseEntity<Long> chatRoomAdd(@RequestBody AddChatRoomForm form) {
        return ResponseEntity.ok(chatRoomRegisterService.addChatRoom(form));
    }

    @GetMapping("/api/chat/room")
    public ResponseEntity<List<ChatRoomDto>> getChatRoom(@RequestParam Long memberId) {
        return ResponseEntity.ok(chatRoomListService.getChatRoomList(memberId));
    }
}
