package com.hotsix.iAmNotAlone.domain.membership.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.ALREADY_EXIST_NICKNAME;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_REGION;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipOAuthForm;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.personality.entity.Personality;
import com.hotsix.iAmNotAlone.domain.personality.repository.PersonalityRepository;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.domain.region.repository.RegionRepository;
import com.hotsix.iAmNotAlone.global.auth.jwt.JwtService;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.exception.business.ErrorCode;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OAuthSignUpService {

    private final MembershipRepository membershipRepository;
    private final RegionRepository regionRepository;
    private final JwtService jwtService;
    private final PersonalityRepository personalityRepository;


    @Transactional
    public Map<String, String> oAuthSignUp(AddMembershipOAuthForm form, Long id) {
        Region region = regionRepository.findById(form.getRegionId()).orElseThrow(
            () -> new BusinessException(NOT_FOUND_REGION));

        Membership membership = membershipRepository.findById(id).orElseThrow(
            () -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        String accessToken = jwtService.createAccessToken(membership.getId(),
            membership.getEmail());
        String refreshToken = jwtService.createRefreshToken();

        Map<String, String> map = jwtService.sendAccessAndRefreshToken(accessToken, refreshToken);

        jwtService.updateRefreshToken(membership.getEmail(), refreshToken);


        // 회원 성향 생성 (개인성향, 선호성향)
        Personality personality = Personality.from(form.getPersonality());
        Personality savedPersonality = personalityRepository.save(personality);

        membership.updateMembershipAuth(form, region, savedPersonality);

        return map;
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