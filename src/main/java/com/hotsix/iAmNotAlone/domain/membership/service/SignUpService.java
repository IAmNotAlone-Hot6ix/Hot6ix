package com.hotsix.iAmNotAlone.domain.membership.service;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.S3FileDto;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipForm;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.region.repository.RegionRepository;
import com.hotsix.iAmNotAlone.global.util.S3UploadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpService {

    private final MembershipRepository membershipRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3UploadService s3UploadService;

    @Transactional
    public Membership signUp(AddMembershipForm form, List<MultipartFile> multipartFiles) {
        Region region = regionRepository.findById(form.getRegionId()).orElseThrow(
            () -> new IllegalArgumentException("일치하는 지역이 없습니다. 지역을 선택해주세요.")
        );

        if (!form.isVerify()) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }

        String url = "";
        if (multipartFiles.get(0).getSize() != 0) {
            List<S3FileDto> s3FileDtos = s3UploadService.uploadFiles(multipartFiles);
            url = s3FileDtos.get(0).getUploadFileUrl();
        }

        String password = passwordEncoder.encode(form.getPassword());
        return membershipRepository.save(Membership.of(form, region, password, url));
    }
}
