package com.hotsix.iAmNotAlone.domain.membership.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.MembershipDetailDto;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MembershipDetailService {

    //    private final MembershipQueryRepository membershipQueryRepository;
    private final MembershipRepository membershipRepository;

    public MembershipDetailDto findMembership(Long userId){
        Membership membership = membershipRepository.findByIdMembership(userId).orElseThrow(
            () -> new BusinessException(NOT_FOUND_USER)
        );
        return new MembershipDetailDto(membership);
    }

}
