package com.hotsix.iAmNotAlone.domain.membership.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.ALREADY_EXIST_NICKNAME;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.ALREADY_EXIST_USER;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_REGION;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_VERIFY_AUTH;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipForm;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.personality.entity.Personality;
import com.hotsix.iAmNotAlone.domain.personality.repository.PersonalityRepository;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.domain.region.repository.RegionRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.util.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SignUpService {

    private final MembershipRepository membershipRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3UploadService s3UploadService;
    private final PersonalityRepository personalityRepository;


    @Transactional
    public Long signUp(AddMembershipForm form, MultipartFile multipartFile) {
        Region region = regionRepository.findById(form.getRegionId()).orElseThrow(
            () -> new BusinessException(NOT_FOUND_REGION)
        );
        if (!form.isVerify()) {
            throw new BusinessException(NOT_VERIFY_AUTH);
        }

        String url = "";
        if (multipartFile.getSize() != 0) {
            url = s3UploadService.uploadFile(multipartFile).getUploadFileUrl();
            log.info(s3UploadService.uploadFile(multipartFile).toString());
        }

        String password = passwordEncoder.encode(form.getPassword());

        // 회원 성향 생성 (개인성향, 선호성향)
        Personality personality = Personality.from(form.getPersonality());
        Personality savedPersonality = personalityRepository.save(personality);

        // 회원 저장 데이터
        Membership membership = Membership.of(form, region, password, url, savedPersonality);
        Membership savedMembership = membershipRepository.save(membership);

        return savedMembership.getId();
    }

    /**
     * 인증 메일 전송시 이메일 중복 검증
     */
    public void validateDuplicateMail(String email) {
        if (membershipRepository.existsByEmail(email)) {
            throw new BusinessException(ALREADY_EXIST_USER);
        }
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
