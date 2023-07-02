package com.hotsix.iAmNotAlone.domain.membership.service;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipOAuthForm;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.domain.region.repository.RegionRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.exception.business.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.ALREADY_EXIST_NICKNAME;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_REGION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OAuthSignUpService {

    private final MembershipRepository membershipRepository;
    private final RegionRepository regionRepository;

    @Transactional
    public Long oAuthSignUp(AddMembershipOAuthForm form, Long id) {
        Region region = regionRepository.findById(form.getRegionId()).orElseThrow(
                () -> new BusinessException(NOT_FOUND_REGION)
        );

        Membership membership = membershipRepository.findById(id).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_USER)
        );

        membership.updateMembership(form, region);

        return membership.getId();
    }

    /**
     * 닉네임 중복 검사
     */
    public boolean validateNickname(String nickname) {
        if (membershipRepository.existsByNickname(nickname)) {
            throw new BusinessException(ALREADY_EXIST_NICKNAME);
        }
        return true;
    }
}