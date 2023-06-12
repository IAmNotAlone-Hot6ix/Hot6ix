package com.hotsix.iAmNotAlone.signup.service;

import com.hotsix.iAmNotAlone.signup.domain.Member;
import com.hotsix.iAmNotAlone.signup.domain.Region;
import com.hotsix.iAmNotAlone.signup.domain.dto.AddMemberDto;
import com.hotsix.iAmNotAlone.signup.domain.dto.S3FileDto;
import com.hotsix.iAmNotAlone.signup.repository.MemberRepository;
import com.hotsix.iAmNotAlone.signup.repository.RegionRepository;
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

    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3UploadService s3UploadService;

    @Transactional
    public Member signUp(AddMemberDto form, List<MultipartFile> multipartFiles) {
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
        return memberRepository.save(Member.of(form, region, password, url));
    }
}
