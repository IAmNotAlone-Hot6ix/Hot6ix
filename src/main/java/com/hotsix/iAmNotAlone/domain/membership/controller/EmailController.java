package com.hotsix.iAmNotAlone.domain.membership.controller;

import com.hotsix.iAmNotAlone.domain.membership.model.form.EmailRequestForm;
import com.hotsix.iAmNotAlone.domain.membership.model.form.EmailVerifyForm;
import com.hotsix.iAmNotAlone.domain.membership.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<String> sendAuthMail(@RequestBody EmailRequestForm emailRequestForm) {
        emailService.sendMail(emailRequestForm);
        return ResponseEntity.ok("이메일이 발송 되었습니다. 이메일을 확인해 주세요.");
    }

    @PostMapping("/email/verify")
    public ResponseEntity<String> verifyEmailAuth(@RequestBody EmailVerifyForm emailVerifyForm) {
        emailService.verifyMail(emailVerifyForm.getEmail(), emailVerifyForm.getCode());
        return ResponseEntity.ok("인증이 완료되었습니다.");
    }
}
