package com.hotsix.iAmNotAlone.signup.controller;

import com.hotsix.iAmNotAlone.signup.domain.dto.AddMemberDto;
import com.hotsix.iAmNotAlone.signup.domain.dto.MemberDto;
import com.hotsix.iAmNotAlone.signup.service.SignUpService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping(value = "/api/user/signup", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MemberDto> signUp(@RequestPart AddMemberDto form,
        @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {
        return ResponseEntity.ok(MemberDto.from(signUpService.signUp(form, multipartFiles)));
    }
}
