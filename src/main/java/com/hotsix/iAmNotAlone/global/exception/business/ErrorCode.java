package com.hotsix.iAmNotAlone.global.exception.business;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 예외처리 문구
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "해당 회원정보가 없습니다.");


    private final HttpStatus httpStatus;
    private final String detail;
}
