package com.hotsix.iAmNotAlone.domain.membership.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_PERSONALITY;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_REGION;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.S3FileDto;
import com.hotsix.iAmNotAlone.domain.membership.model.form.ModifyMembershipForm;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.personality.entity.Personality;
import com.hotsix.iAmNotAlone.domain.personality.repository.PersonalityRepository;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.domain.region.repository.RegionRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.util.S3UploadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MembershipModifyService {

    private final MembershipRepository membershipRepository;
    private final RegionRepository regionRepository;
    private final S3UploadService s3UploadService;
    private final PersonalityRepository personalityRepository;


    /**
     * 회원 수정
     */
    @Transactional
    public Long modifyMembership(Long user_id, ModifyMembershipForm form,
        List<MultipartFile> multipartFiles) {

        Membership member = membershipRepository.findById(user_id)
            .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        Region region = regionRepository.findById(form.getRegionId())
            .orElseThrow(() ->
                new BusinessException(NOT_FOUND_REGION));
        
        // 성향
        Personality personality = personalityRepository.findById(member.getPersonality().getId())
            .orElseThrow(() -> new BusinessException(NOT_FOUND_PERSONALITY));


        // 1. 기존에 파일이 있고, 새로 업로드하는 파일이 있으면 url 바꾸고 기존 파일은 삭제
        // 2. 기존에 파일이 있고, 새로 업로드하는 파일이 없고, 기존파일 삭제
        // 3. 새로 업로드하는 파일이 없고 기존파일 유지

        // 1. 새로 업로드 하는 파일이 있다
        if (multipartFiles.get(0).getSize() != 0) {
            // 1-2. 기존에 파일이 있다. -> 기존 파일 s3 버킷에서 삭제

            if (member.getImgPath().length() != 0) {
                String[] sp = member.getImgPath().split("\\.");

                // 카카오에서 가져온 이미지가 아니면 s3
                if (!sp[1].equals("kakaocdn")) {
                    String[] split = member.getImgPath().split("com/");
                    s3UploadService.deleteFile(split[1]);
                }
            }
            // 1-1. 기존에 파일이 없다. -> 삭제 없이 s3 버킷에 업로드
            List<S3FileDto> s3FileDtos = s3UploadService.uploadFiles(multipartFiles);
            form.setImgPath(s3FileDtos.get(0).getUploadFileUrl());
        } else { // 2. 새로 업로드하느 파일이 없다.
            // 2-2. 기존 파일을 삭제한다.
            if (!form.getImgPath().equals(member.getImgPath())) {
                String[] split = member.getImgPath().split("com/");
                s3UploadService.deleteFile(split[1]);
            }
        }

        // 성향 업데이트
        personality.updateFromDto(form.getPersonality());

        // 2-1. 기존에 파일을 그대로 둔다.
        member.updateMembership(form, region, personality);

        return member.getId();
    }
}
