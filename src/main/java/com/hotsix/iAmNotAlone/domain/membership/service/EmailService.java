package com.hotsix.iAmNotAlone.domain.membership.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.EXPIRE_CODE;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_MATCH_CODE;

import com.hotsix.iAmNotAlone.domain.membership.model.dto.EmailMessage;
import com.hotsix.iAmNotAlone.domain.membership.model.form.EmailRequestForm;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.util.RedisUtil;
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
    private final MembershipService membershipService;
    private final RedisUtil redisUtil;

    /**
     * 이메일 인증번호 발송
     */
    public void sendMail(EmailMessage emailMessage, String type) {
        String authCode = createCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        if (type.equals("password")) {
            membershipService.createTemPassword(emailMessage.getTo(), authCode);
        }

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false,
                "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(setContext(authCode, type), true);
            javaMailSender.send(mimeMessage);
            redisUtil.setDataExpire(emailMessage.getTo(), authCode, 60 * 5);

            log.info("mail send success");
        } catch (MessagingException e) {
            log.info("mail send fail");
            throw new RuntimeException(e);
        }
    }

    /**
     * 인증번호, 임시비밀번호 생성 메서드
     */
    private String createCode() {
        return RandomStringUtils.random(10, true, true);
    }

    /**
     * thymeleaf html적용
     */
    private String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return springTemplateEngine.process(type, context);
    }

    /**
     * 이메일 인증 확인
     */
    public boolean verifyMail(String email, String code) {
        String data = redisUtil.getData(email);
        if (data == null) {
            throw new BusinessException(EXPIRE_CODE);
        } else if (!data.equals(code)) {
            throw new BusinessException(NOT_MATCH_CODE);
        }
        return true;
    }
}
