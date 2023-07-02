package com.hotsix.iAmNotAlone.global.auth.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.global.auth.PrincipalDetails;
import com.hotsix.iAmNotAlone.global.auth.jwt.JwtService;
import com.hotsix.iAmNotAlone.global.util.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static com.hotsix.iAmNotAlone.global.auth.common.Role.USER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final MembershipRepository membershipRepository;
    private final JwtService jwtService;
    private final S3UploadService s3UploadService;
    private final HttpServletResponse servletResponse;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String clientId = userRequest.getClientRegistration().getClientId();

        // 비밀번호는 db에 저장하는 용도 실제 사용자가 쓰지 않음
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

        String url = "";

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) properties.get("nickname");
        String imgPath = (String) properties.get("profile_image");
        String password = passwordEncoder.encode(clientId);

        url = s3UploadService.OAuthUploadFile(imgPath).getUploadFileUrl();

        Optional<Membership> memberOptional = membershipRepository.findByEmail(email);

        if (!memberOptional.isPresent()) {
            return handleNewMembership(email, password, nickname, url, oAuth2User);
        } else {
            return handleExistingMembership(memberOptional.get(), email, oAuth2User);
        }
    }

    private OAuth2User handleNewMembership(String email, String password, String nickname, String imgPath, OAuth2User oAuth2User) {
        Membership membership = Membership.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .imgPath(imgPath)
                .role(USER)
                .build();
        Membership savedMember = membershipRepository.save(membership);

        writeResponse(savedMember.getId().toString());
        try {
            servletResponse.sendRedirect("/socialsignup.html");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new PrincipalDetails(membership, oAuth2User.getAttributes());
    }

    private OAuth2User handleExistingMembership(Membership membership, String email, OAuth2User oAuth2User) {
        ObjectMapper om = new ObjectMapper();
        log.info("소셜 로그인 성공");

        String accessToken = jwtService.createAccessToken(membership.getId(), email);
        String refreshToken = jwtService.createRefreshToken();

        Map<String, String> accessRefreshMap = jwtService.sendAccessAndRefreshToken(servletResponse, accessToken, refreshToken);
        String jsonAccessRefreshMap = null;

        try {
            jsonAccessRefreshMap = om.writeValueAsString(accessRefreshMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        log.info(jsonAccessRefreshMap);
        servletResponse.setContentType(APPLICATION_JSON_VALUE);
        servletResponse.setCharacterEncoding("utf-8");

        writeResponse(jsonAccessRefreshMap);
        writeResponse(membership.getId().toString());
        jwtService.updateRefreshToken(email, refreshToken);

        return new PrincipalDetails(membership, oAuth2User.getAttributes());
    }

    private void writeResponse(String response) {
        try {
            servletResponse.getWriter().write(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}