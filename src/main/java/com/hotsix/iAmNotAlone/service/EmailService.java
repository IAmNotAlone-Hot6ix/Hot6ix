package com.hotsix.iAmNotAlone.service;

import com.hotsix.iAmNotAlone.domain.dto.EmailRequestDto;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final RedisUtil redisUtil;

    /**
     * 이메일 인증번호 발송
     */
    public void sendMail(EmailRequestDto emailRequestDto) {
        String authCode = createCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false,
                "UTF-8");
            mimeMessageHelper.setTo(emailRequestDto.getEmail());
            mimeMessageHelper.setSubject("[나혼자안산다] 이메일 인증 메일 발송");
            mimeMessageHelper.setText(setContext(authCode), true);
            javaMailSender.send(mimeMessage);
            redisUtil.setDataExpire(emailRequestDto.getEmail(), authCode, 60 * 3);

            log.info("mail send success");
        } catch (MessagingException e) {
            log.info("mail send fail");
            throw new RuntimeException(e);
        }
    }

    /**
     * 인증번호 생성 메서드
     */
    private String createCode() {
        return RandomStringUtils.random(10, true, true);
    }

    /**
     * thymeleaf html적용
     */
    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return springTemplateEngine.process("email", context);
    }

    /**
     * 이메일 인증 확인
     */
    public void verifyMail(String email, String code) {
        String data = redisUtil.getData(email);
        if (data == null) {
            throw new IllegalArgumentException("인증시간이 만료되었습니다.");
        } else if (!data.equals(code)) {
            throw new IllegalArgumentException("인증 코드가 일치하지 않습니다.");
        }
    }
}
