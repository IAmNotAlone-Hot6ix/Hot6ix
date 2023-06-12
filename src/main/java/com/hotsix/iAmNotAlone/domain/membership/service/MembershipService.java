package com.hotsix.iAmNotAlone.domain.membership.service;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.S3FileDto;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipForm;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.MemberDto;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.region.repository.RegionRepository;
import com.hotsix.iAmNotAlone.global.util.S3UploadService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3UploadService s3UploadService;

    public List<MemberDto> findAll() {
        List<MemberDto> memberDtos = new ArrayList<>();
        for (Membership m : membershipRepository.findAll()) {
            memberDtos.add(MemberDto.from(m));
        }
        return memberDtos;
    }


    @Transactional
    public Membership update(AddMembershipForm form, Long id, List<MultipartFile> multipartFiles) {
        Membership member = membershipRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Region region = regionRepository.findById(form.getRegionId()).orElseThrow(
            () -> new IllegalArgumentException("일치하는 지역이 없습니다. 지역을 선택해주세요.")
        );

        List<S3FileDto> s3FileDtos = s3UploadService.uploadFiles(multipartFiles);
        s3FileDtos.get(0).getUploadFileUrl();

        String password = passwordEncoder.encode(form.getPassword());
        return membershipRepository.save(Membership.of(form, region, password, s3FileDtos.get(0).getUploadFileUrl()));
    }

}
