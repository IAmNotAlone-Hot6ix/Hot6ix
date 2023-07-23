package com.hotsix.iAmNotAlone.domain.membership.service;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.MembershipDetailDto;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.MembershipSummaryDto;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MembershipDetailService {

    private final MembershipRepository membershipRepository;

    public MembershipDetailDto findMembership(Long userId) {
        Membership membership = membershipRepository.findByIdMembership(userId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_USER)
        );
        return new MembershipDetailDto(membership);
    }

    public MembershipSummaryDto findSender(Long senderId) {
        return MembershipSummaryDto.from(membershipRepository.findById(senderId).orElseThrow(
            () -> new BusinessException(NOT_FOUND_USER)
        ));
    }

}
