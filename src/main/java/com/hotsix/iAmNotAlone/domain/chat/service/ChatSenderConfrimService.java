package com.hotsix.iAmNotAlone.domain.chat.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatSenderConfrimService {

    private final MembershipRepository membershipRepository;

    public Membership getSender(Long senderId) {
        return membershipRepository.findById(senderId).orElseThrow(
            () -> new BusinessException(NOT_FOUND_USER)
        );
    }
}
