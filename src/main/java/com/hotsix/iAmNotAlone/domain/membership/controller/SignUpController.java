package com.hotsix.iAmNotAlone.domain.membership.controller;

import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipForm;
import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipOAuthForm;
import com.hotsix.iAmNotAlone.domain.membership.model.form.VerifyNicknameForm;
import com.hotsix.iAmNotAlone.domain.membership.service.OAuthSignUpService;
import com.hotsix.iAmNotAlone.domain.membership.service.SignUpService;
import com.hotsix.iAmNotAlone.global.auth.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final OAuthSignUpService oAuthSignUpService;
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
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refresh = jwtService.refresh(request, response);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(refresh);
    }

    @PatchMapping("/oauth/signup/{id}")
    public ResponseEntity<Long> oauthSignUp(@RequestBody AddMembershipOAuthForm form, @PathVariable Long id) {
        return ResponseEntity.ok(oAuthSignUpService.oAuthSignUp(form, id));
    }

    @PostMapping("/oauth/token/{memberId}")
    public ResponseEntity<Map<String, String>> token(HttpServletResponse response, @PathVariable Long memberId){
        String email = jwtService.extractEmail(memberId);
        String accessToken = jwtService.createAccessToken(memberId, email);
        String refreshToken = jwtService.createRefreshToken();
        Map<String, String> token = jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(email,refreshToken);
        return ResponseEntity.ok(token);
    }

}
