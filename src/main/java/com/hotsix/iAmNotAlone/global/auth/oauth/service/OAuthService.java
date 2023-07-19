package com.hotsix.iAmNotAlone.global.auth.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.global.auth.jwt.JwtService;
import com.hotsix.iAmNotAlone.global.auth.oauth.dto.KakaoAccountDto;
import com.hotsix.iAmNotAlone.global.auth.oauth.dto.KakaoTokenDto;
import com.hotsix.iAmNotAlone.global.auth.oauth.dto.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static com.hotsix.iAmNotAlone.global.auth.common.Role.USER;


@Service
@RequiredArgsConstructor
public class OAuthService {

    private final MembershipRepository membershipRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${KAKAO_REST_API_KEY}")
    private String kakaoRestApiKey;
    @Value("${KAKAO_SECRET_KEY}")
    private String kakaoSecretKey;
//    @Value("${KAKAO_REDIRECT_KEY}")
//    private String kakaoRedirectKey;
//    @Value("${KAKAO_TOKEN_KEY}")
//    private String kakaoTokenKey;
//    @Value("${KAKAO_INFO_KEY}")
//    private String kakaoInfoKey;

    private String kakaoRedirectKey="https://hot-six-fe.vercel.app";
    private String kakaoTokenKey="https://kauth.kakao.com/oauth/token";
    private String kakaoInfoKey="https://kapi.kakao.com/v2/user/me";

    @Transactional
    public KakaoTokenDto getKakaoAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); //카카오 공식문서 기준 authorization_code 로 고정
        params.add("client_id", kakaoRestApiKey); // 카카오 Dev 앱 REST API 키
        params.add("redirect_uri", kakaoRedirectKey); // 카카오 Dev redirect uri
        params.add("code", code); // 프론트에서 인가 코드 요청시 받은 인가 코드값
        params.add("client_secret", kakaoSecretKey); // 카카오 Dev 카카오 로그인 Client Secret

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // 카카오로부터 Access token 받아오기
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                kakaoTokenKey,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KakaoTokenDto kakaoTokenDto = null;
        try {
            kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoTokenDto;
    }

    public Membership getKakaoInfo(String kakaoAccessToken) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);
        // POST 방식으로 API 서버에 요청 후 response 받아옴
        ResponseEntity<String> accountInfoResponse = rt.exchange(
                kakaoInfoKey,
                HttpMethod.POST,
                accountInfoRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KakaoAccountDto kakaoAccountDto = null;

        try {
            kakaoAccountDto = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String email = kakaoAccountDto.getKakaoAccount().getEmail();
        String nickname = kakaoAccountDto.getKakaoAccount().getProfile().getNickname();
        String profileImageUrl = kakaoAccountDto.getKakaoAccount().getProfile().getProfileImageUrl();

        // 회원가입 처리하기
        Optional<Membership> membershipOptional = membershipRepository.findByEmail(email);
        // 처음 로그인이 아닌 경우
        Membership membership;

        String password = passwordEncoder.encode("iamnotalone");
        Region region = Region.builder().id(1L).build();

        if (membershipOptional.isEmpty()) {
            membership = Membership.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .imgPath(profileImageUrl)
                    .role(USER)
                    .region(region)
                    .build();
            membershipRepository.save(membership);
            return membership;
        } else {
            return membershipOptional.get();
        }
    }

    @ResponseBody
    public ResponseEntity<LoginResponseDto> kakaoLogin(String kakaoAccessToken) {
        Membership membership = getKakaoInfo(kakaoAccessToken);

        Long id = membership.getId();
        String email = membership.getEmail();
        Optional<LocalDate> birthOptional = Optional.ofNullable(membership.getBirth());
        String accessToken = jwtService.createAccessToken(id, email);
        String refreshToken = jwtService.createRefreshToken();

        LoginResponseDto loginResponseDto = new LoginResponseDto();

        loginResponseDto.setAccessToken(accessToken);
        loginResponseDto.setRefreshToken(refreshToken);

        if (birthOptional.isEmpty()) {
            loginResponseDto.setFirstLogin(true);
            return ResponseEntity.ok().body(loginResponseDto);
        } else {
            loginResponseDto.setFirstLogin(false);
            jwtService.updateRefreshToken(email, refreshToken);
            return ResponseEntity.ok().body(loginResponseDto);
        }
    }
}
