package com.hotsix.iAmNotAlone.controller;

import com.hotsix.iAmNotAlone.domain.dto.AddMemberDto;
import com.hotsix.iAmNotAlone.domain.dto.MemberDto;
import com.hotsix.iAmNotAlone.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/api/user/signup")
    public ResponseEntity<MemberDto> signUp(@RequestBody AddMemberDto form) {
        return ResponseEntity.ok(MemberDto.from(signUpService.signUp(form)));
    }
}
