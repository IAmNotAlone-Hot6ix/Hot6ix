package com.hotsix.iAmNotAlone.domain.membership.controller;

import com.hotsix.iAmNotAlone.domain.membership.model.dto.EmailMessage;
import com.hotsix.iAmNotAlone.domain.membership.model.form.EmailRequestForm;
import com.hotsix.iAmNotAlone.domain.membership.model.form.EmailVerifyForm;
import com.hotsix.iAmNotAlone.domain.membership.service.EmailService;
import com.hotsix.iAmNotAlone.domain.membership.service.MembershipService;
import com.hotsix.iAmNotAlone.domain.membership.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/email")
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final SignUpService signUpService;

    @PostMapping("/auth")
    public ResponseEntity<String> sendAuthMail(@RequestBody EmailRequestForm emailRequestForm) {
        signUpService.validateDuplicateMail(emailRequestForm.getEmail());

        EmailMessage emailMessage = EmailMessage.builder()
            .to(emailRequestForm.getEmail())
            .subject("[나혼자안산다] 이메일 인증 메일 발송")
            .build();

        emailService.sendMail(emailMessage, "email");
        return ResponseEntity.ok("인증번호가 이메일로 발송 되었습니다. 이메일을 확인해 주세요.");
    }

    @PostMapping("/password")
    public ResponseEntity<String> sendPassword(@RequestBody EmailRequestForm emailRequestForm) {
        EmailMessage emailMessage = EmailMessage.builder()
            .to(emailRequestForm.getEmail())
            .subject("[나혼자안산다] 임시 비밀번호 발급")
            .build();

        emailService.sendMail(emailMessage, "password");
        return ResponseEntity.ok("임시 비밀번호가 이메일로 발송 되었습니다. 이메일을 확인해 주세요.");
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyEmailAuth(@RequestBody EmailVerifyForm emailVerifyForm) {
        return ResponseEntity.ok(emailService.verifyMail(emailVerifyForm.getEmail(), emailVerifyForm.getCode()));
    }
}
