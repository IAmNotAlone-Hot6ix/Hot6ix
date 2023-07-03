package com.hotsix.iAmNotAlone.global.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.exception.business.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.hotsix.iAmNotAlone.global.auth.jwt.JwtProperties.*;

@Transactional
@Service
@RequiredArgsConstructor
@Setter(value = AccessLevel.PRIVATE)
@Slf4j
public class JwtService {

    private final MembershipRepository membershipRepository;

    public String createAccessToken(Long id, String email) {
        log.info("어세스 토큰 생성");
        String token = JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME * 1000))
                .withClaim("email", email)
                .withClaim("id", id)
                .sign(Algorithm.HMAC512(SECRET));

        return TOKEN_PREFIX + token;
    }

    public String createRefreshToken() {
        log.info("리프래시 토큰 생성");
        String token = JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME * 1000))
                .sign(Algorithm.HMAC512(SECRET));

        return TOKEN_PREFIX + token;
    }

    public void updateRefreshToken(String email, String refreshToken) {
        membershipRepository.findByEmail(email)
                .ifPresentOrElse(
                        member -> member.updateRefreshToken(refreshToken),
                        () -> new Exception("회원이 없습니다")
                );
    }


    // 추후 사용
    public void destroyRefreshToken(String email) {
        membershipRepository.findByEmail(email)
                .ifPresentOrElse(
                        member -> member.destroyRefreshToken(),
                        () -> new Exception("회원이 없습니다")
                );
    }

    public Map<String, String> sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
        tokenMap.put(REFRESH_TOKEN_SUBJECT, refreshToken);

        return tokenMap;
    }
    public Map<String, String> sendURLAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, String> tokenMap = new HashMap<>();
        try {
            String encodedAccessToken = URLEncoder.encode(accessToken, StandardCharsets.UTF_8.toString());
            String encodedRefreshToken = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8.toString());
            tokenMap.put(ACCESS_TOKEN_SUBJECT, encodedAccessToken);
            tokenMap.put(REFRESH_TOKEN_SUBJECT, encodedRefreshToken);
            return tokenMap;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

        public Map<String, String> sendURLAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, String> tokenMap = new HashMap<>();
        try {
            String encodedAccessToken = URLEncoder.encode(accessToken, StandardCharsets.UTF_8.toString());
            String encodedRefreshToken = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8.toString());
            tokenMap.put(ACCESS_TOKEN_SUBJECT, encodedAccessToken);
            tokenMap.put(REFRESH_TOKEN_SUBJECT, encodedRefreshToken);
            return tokenMap;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    public Map<String, String> sendAccessToken(HttpServletResponse response, String accessToken) {

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
        return tokenMap;
    }


    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(ACCESS_TOKEN_SUBJECT)).filter(
                accessToken -> accessToken.startsWith(TOKEN_PREFIX)
        ).map(accessToken -> accessToken.replace(TOKEN_PREFIX, ""));
    }

    public Optional<String> extractUsername(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(SECRET)).build().verify(accessToken).getClaim("email").asString());

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(SECRET)).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper om = new ObjectMapper();
        log.info("refresh 유효성 확인");
        String refreshToken = request.getHeader(REFRESH_TOKEN_SUBJECT);
        String refreshTokenBearerRemove = refreshToken.replace(TOKEN_PREFIX, "");
        boolean refreshTokenValid = this.isTokenValid(refreshTokenBearerRemove);

        if (refreshTokenValid) {
            log.info("refresh token not null");

            Optional<Membership> optionalToken = membershipRepository.findByRefreshToken(refreshToken);
            if (optionalToken.isPresent()) {
                Membership member = optionalToken.get();
                Map<String, String> map1 = sendAccessToken(response, createAccessToken(member.getId(), member.getEmail()));
                return om.writeValueAsString(map1);
            }
        } else {
            log.info("refresh token null 이면 재로그인");
            response.getWriter().write("토큰시간 유효시간이 지났습니다. 재로그인 해주세요");
        }
        return "";
    }

    public String extractEmail(Long memberId) {
        Membership membership = membershipRepository.findById(memberId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_USER)
        );

        return membership.getEmail();
    }

}
