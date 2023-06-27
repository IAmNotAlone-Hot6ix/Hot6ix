package com.hotsix.iAmNotAlone.domain.chat.service;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatRoom;
import com.hotsix.iAmNotAlone.domain.chat.dto.AddChatMessageFrom;
import com.hotsix.iAmNotAlone.domain.chat.dto.AddChatRoomForm;
import com.hotsix.iAmNotAlone.domain.chat.repository.ChatRoomRepository;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.MembershipSummaryDto;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.membership.service.MembershipDetailService;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.exception.business.ErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomRegisterService {

    private final ChatRoomRepository chatRoomRepository;
    private final MembershipRepository membershipRepository;

    @Transactional
    public Long addChatRoom(AddChatRoomForm form) {


        // 채팅방이 존재하면 해당 채팅방 아이디 리턴
        Long roomId = validateChatRoom(form);
        if (roomId != 0) {return roomId;}

        // 채팅방이 존재하지 않으면 생성
        Membership sender = membershipRepository.findById(form.getSenderId()).orElseThrow(
            () -> new BusinessException(ErrorCode.NOT_FOUND_USER)
        );
        Membership receiver = membershipRepository.findById(form.getReceiverId()).orElseThrow(
            () -> new BusinessException(ErrorCode.NOT_FOUND_USER)
        );

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.from(sender, receiver));

        return chatRoom.getId();
    }

    private Long validateChatRoom(AddChatRoomForm form) {

        Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderIdAndReceiverId(
            form.getSenderId(), form.getReceiverId());
        if (chatRoom.isPresent()) {
            return chatRoom.get().getId();
        }

        return 0L;
    }

}
