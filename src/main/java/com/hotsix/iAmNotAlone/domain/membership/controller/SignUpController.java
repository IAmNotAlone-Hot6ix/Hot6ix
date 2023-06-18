package com.hotsix.iAmNotAlone.domain.membership.controller;

import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipForm;
import com.hotsix.iAmNotAlone.domain.membership.model.form.VerifyNicknameForm;
import com.hotsix.iAmNotAlone.domain.membership.service.SignUpService;
import com.hotsix.iAmNotAlone.global.auth.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;
    private final JwtService jwtService;

    @PostMapping(value = "/signup", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Long> signUp(@RequestPart AddMembershipForm form,
        @RequestPart(value = "files", required = false) MultipartFile multipartFile) {
        return ResponseEntity.ok(signUpService.signUp(form, multipartFile));
    }

    @PostMapping("/nickname")
    public ResponseEntity<Boolean> validateNickname(@RequestBody VerifyNicknameForm form) {
        return ResponseEntity.ok(signUpService.validateNickname(form.getNickname()));
    }

    @PostMapping("/refresh")
    public void refresh(HttpServletRequest request , HttpServletResponse response) throws IOException {
        String refresh = jwtService.refresh(request, response);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(refresh);
    }
}
