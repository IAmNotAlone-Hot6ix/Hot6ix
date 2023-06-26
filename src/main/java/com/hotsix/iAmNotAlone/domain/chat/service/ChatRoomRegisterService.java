package com.hotsix.iAmNotAlone.domain.chat.service;

import com.hotsix.iAmNotAlone.domain.chat.domain.ChatRoom;
import com.hotsix.iAmNotAlone.domain.chat.dto.AddChatRoomForm;
import com.hotsix.iAmNotAlone.domain.chat.repository.ChatRoomRepository;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.MembershipSummaryDto;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.membership.service.MembershipDetailService;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.exception.business.ErrorCode;
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

        Membership sender = membershipRepository.findById(form.getSenderId()).orElseThrow(
            () -> new BusinessException(ErrorCode.NOT_FOUND_USER)
        );

        Membership receiver = membershipRepository.findById(form.getReceiverId()).orElseThrow(
            () -> new BusinessException(ErrorCode.NOT_FOUND_USER)
        );

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.from(sender, receiver));

        return chatRoom.getId();
    }

}
