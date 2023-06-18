package com.hotsix.iAmNotAlone.domain.membership.service;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.MembershipDetailDto;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MembershipDetailService {

    private final MembershipQueryRepository membershipQueryRepository;

    public MembershipDetailDto my(Long userId){
        Membership membership = membershipQueryRepository.findByIdMembership(userId);
        return new MembershipDetailDto(membership);
    }

}
