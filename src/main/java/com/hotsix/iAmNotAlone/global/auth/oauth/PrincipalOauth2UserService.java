package com.hotsix.iAmNotAlone.global.auth.oauth;

import static com.hotsix.iAmNotAlone.global.auth.common.Role.USER;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.global.auth.PrincipalDetails;
import com.hotsix.iAmNotAlone.global.auth.jwt.JwtService;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

import com.hotsix.iAmNotAlone.global.auth.oauth.dto.TokenResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final MembershipRepository membershipRepository;
    private final JwtService jwtService;
    private final HttpServletResponse servletResponse;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String clientId = userRequest.getClientRegistration().getClientId();

        // 비밀번호는 db에 저장하는 용도 실제 사용자가 쓰지 않음
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) properties.get("nickname");
        String imgPath = (String) properties.get("profile_image");
        log.info(imgPath);
        String password = passwordEncoder.encode(clientId);

        Optional<Membership> memberOptional = membershipRepository.findByEmail(email);

        Region region = Region.builder().id(1L).build();

        if (!memberOptional.isPresent()) {
            return handleNewMembership(email, password, nickname, imgPath, region, oAuth2User);
        } else {
            return handleExistingMembership(memberOptional.get(), email, oAuth2User);
        }
    }

    private OAuth2User handleNewMembership(String email, String password, String nickname, String imgPath, Region region, OAuth2User oAuth2User) {
        Membership membership = Membership.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .imgPath(imgPath)
                .role(USER)
                .region(region)
                .build();
        Membership savedMember = membershipRepository.save(membership);

        try {

            servletResponse.sendRedirect("https://iamnotalone.vercel.app/socialsignup/"+savedMember.getId());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new PrincipalDetails(membership, oAuth2User.getAttributes());
    }

    private OAuth2User handleExistingMembership(Membership membership, String email, OAuth2User oAuth2User) {
        log.info("소셜 로그인 성공");

        String accessToken = jwtService.createAccessToken(membership.getId(), email);
        String refreshToken = jwtService.createRefreshToken();

        // 토큰 정보를 담은 DTO 생성
        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO(accessToken, refreshToken);

        try {
            // 토큰 정보를 JSON으로 변환
            String jsonResponse = new ObjectMapper().writeValueAsString(tokenResponseDTO);

            // HTTP 응답 설정
            servletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.getWriter().write(jsonResponse);
            servletResponse.setHeader("Authorization",accessToken);
            servletResponse.setHeader("oauth",accessToken);
            jwtService.updateRefreshToken(email, refreshToken);
            servletResponse.sendRedirect("https://iamnotalone.vercel.app/main");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new PrincipalDetails(membership, oAuth2User.getAttributes());
    }
}
