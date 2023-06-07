package com.hotsix.iAmNotAlone.controller;

import com.hotsix.iAmNotAlone.domain.dto.MemberDto;
import com.hotsix.iAmNotAlone.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/members")
    public List<MemberDto> findAll() {
        return memberService.findAll();
    }
}
