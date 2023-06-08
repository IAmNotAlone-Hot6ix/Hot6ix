package com.hotsix.iAmNotAlone.service;

import com.hotsix.iAmNotAlone.domain.Member;
import com.hotsix.iAmNotAlone.domain.dto.MemberDto;
import com.hotsix.iAmNotAlone.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public List<MemberDto> findAll() {
        List<MemberDto> memberDtos = new ArrayList<>();
        for (Member m : memberRepository.findAll()) {
            memberDtos.add(MemberDto.from(m));
        }
        return memberDtos;
    }

}
