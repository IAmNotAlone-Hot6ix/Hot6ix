package com.hotsix.iAmNotAlone.domain.membership.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.region.repository.RegionRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.util.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MembershipRemoveService {

    private final MembershipRepository membershipRepository;
    private final RegionRepository regionRepository;
    private final S3UploadService s3UploadService;

    /**
     * 회원 삭제
     */
    @Transactional
    public void removeMembership(Long id) {
        // 유저 정보에 이미지가 있으면 s3에 업로드된 이미지 삭제
        Membership membership = membershipRepository.findById(id).orElseThrow(
            () -> new BusinessException(NOT_FOUND_USER)
        );
        if (membership.getImgPath().length() != 0) {
            String[] split = membership.getImgPath().split("com/");
            s3UploadService.deleteFile(split[1]);
        }

        membershipRepository.deleteById(id);
    }
}
