package com.hotsix.iAmNotAlone.domain.membership.controller;

import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipForm;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.MemberDto;
import com.hotsix.iAmNotAlone.domain.membership.model.form.NicknameVerifyForm;
import com.hotsix.iAmNotAlone.domain.membership.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping(value = "/signup", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MemberDto> signUp(@RequestPart AddMembershipForm form,
        @RequestPart(value = "files", required = false) MultipartFile multipartFile) {
        return ResponseEntity.ok(MemberDto.from(signUpService.signUp(form, multipartFile)));
    }

    @PostMapping("/nickname")
    public ResponseEntity<Boolean> validateNickname(@RequestBody NicknameVerifyForm form) {
        return ResponseEntity.ok(signUpService.validateNickname(form.getNickname()));
    }
}
