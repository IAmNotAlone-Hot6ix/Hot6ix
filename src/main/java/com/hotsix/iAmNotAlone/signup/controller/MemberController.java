package com.hotsix.iAmNotAlone.signup.controller;

import com.hotsix.iAmNotAlone.signup.domain.dto.MemberDto;
import com.hotsix.iAmNotAlone.signup.domain.dto.UpdateMemberDto;
import com.hotsix.iAmNotAlone.signup.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/members")
    public List<MemberDto> findAll() {
        return memberService.findAll();
    }

    @PutMapping("/api/member/update/{id}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable("id") Long id,
        @RequestBody UpdateMemberDto updateMemberDto) {

        return ResponseEntity.ok().build();
    }
}
