package com.hotsix.iAmNotAlone.global.exception.business;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 예외처리 문구
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "해당 회원정보가 없습니다."),

    // 회원가입
    ALREADY_EXIST_USER(HttpStatus.BAD_REQUEST, "해당 이메일이 존재합니다."),
    ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "해당 닉네임이 존재합니다."),
    NOT_FOUND_REGION(HttpStatus.BAD_REQUEST, "지역을 선택해주세요."),
    NOT_VERIFY_AUTH(HttpStatus.BAD_REQUEST, "이메일 인증을 진행해주세요."),


    // 이메일 인증 확인
    EXPIRE_CODE(HttpStatus.BAD_REQUEST, "인증시간이 만료되었습니다."),
    NOT_MATCH_CODE(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),

    //비밀번호 찾기
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "해당 이메일로 가입된 회원이 존재하지 않습니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_MATCH_PASSWORD_VERIFY(HttpStatus.BAD_REQUEST, "새비밀번호와 비밀번호 확인이 일치하지 않습니다."),

    ;
    private final HttpStatus httpStatus;
    private final String detail;
}
