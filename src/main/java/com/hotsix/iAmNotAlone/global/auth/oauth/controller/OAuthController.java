package com.hotsix.iAmNotAlone.global.auth.oauth.controller;

import com.hotsix.iAmNotAlone.global.auth.oauth.dto.LoginResponseDto;
import com.hotsix.iAmNotAlone.global.auth.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oauthService;

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<LoginResponseDto> kakaoLogin(HttpServletRequest request) {
        String code = request.getParameter("code");
        String kakaoAccessToken = oauthService.getKakaoAccessToken(code).getAccess_token();

        return oauthService.kakaoLogin(kakaoAccessToken);
    }
}
