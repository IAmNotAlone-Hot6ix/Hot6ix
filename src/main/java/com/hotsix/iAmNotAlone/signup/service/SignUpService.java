package com.hotsix.iAmNotAlone.signup.service;

import com.hotsix.iAmNotAlone.signup.domain.Member;
import com.hotsix.iAmNotAlone.signup.domain.Region;
import com.hotsix.iAmNotAlone.signup.domain.dto.AddMemberDto;
import com.hotsix.iAmNotAlone.signup.repository.MemberRepository;
import com.hotsix.iAmNotAlone.signup.repository.RegionRepository;
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
        String password = passwordEncoder.encode(form.getPassword());
        return memberRepository.save(Member.of(form, region, password));
    }
}
