package com.hotsix.iAmNotAlone.global.auth.oauth.controller;

import com.hotsix.iAmNotAlone.global.auth.oauth.dto.LoginResponseDto;
import com.hotsix.iAmNotAlone.global.auth.oauth.form.OAuth2CodeForm;
import com.hotsix.iAmNotAlone.global.auth.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oauthService;

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestBody OAuth2CodeForm form) {
//        String code = request.getParameter("code");
        String code = form.getCode();
        log.info("code==================================="+code);
        String kakaoAccessToken = oauthService.getKakaoAccessToken(code).getAccess_token();
        log.info("카카오톡 어세스 토큰 발급완료");
        return oauthService.kakaoLogin(kakaoAccessToken);
    }
}
