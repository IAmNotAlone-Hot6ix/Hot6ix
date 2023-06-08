package com.hotsix.iAmNotAlone.service;

import com.hotsix.iAmNotAlone.domain.Member;
import com.hotsix.iAmNotAlone.domain.Region;
import com.hotsix.iAmNotAlone.domain.dto.AddMemberDto;
import com.hotsix.iAmNotAlone.repository.MemberRepository;
import com.hotsix.iAmNotAlone.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpService {

    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member signUp(AddMemberDto form) {
        Region region = regionRepository.findById(form.getRegionId()).orElseThrow(
            () -> new IllegalArgumentException("일치하는 지역이 없습니다. 지역을 선택해주세요.")
        );
        form.setPassword(passwordEncoder.encode(form.getPassword()));
        return memberRepository.save(Member.from(form, region));
    }
}
