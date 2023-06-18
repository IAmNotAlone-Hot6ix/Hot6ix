package com.hotsix.iAmNotAlone.domain.membership.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_EXIST_USER;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_MATCH_PASSWORD;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_MATCH_PASSWORD_VERIFY;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.form.ModifyPasswordForm;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordModifyService {

    private final MembershipRepository membershipRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void modifyPassword(Long user_id, ModifyPasswordForm form) {
        Membership membership = membershipRepository.findById(user_id).orElseThrow(
            () -> new BusinessException(NOT_FOUND_USER)
        );
        // 비밀번호 검증
        if (!passwordEncoder.encode(form.getPassword()).equals(membership.getPassword())) {
            throw new BusinessException(NOT_MATCH_PASSWORD);
        } else if (!form.getNewPassword().equals(form.getNewPasswordVerify())) {
            throw new BusinessException(NOT_MATCH_PASSWORD_VERIFY);
        }
        membership.updatePassword(passwordEncoder.encode(form.getNewPassword()));
    }

    /**
     * 임시 비밀번호 발급
     */
    @Transactional
    public void createTemPassword(String email, String password) {
        Membership membership = membershipRepository.findByEmail(email).orElseThrow(
            () -> new BusinessException(NOT_EXIST_USER)
        );
        membership.updatePassword(passwordEncoder.encode(password));
    }

}
