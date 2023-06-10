package com.hotsix.iAmNotAlone.login.config.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hotsix.iAmNotAlone.login.config.auth.PrincipalDetails;
import com.hotsix.iAmNotAlone.login.config.jwt.JwtProperties;
import com.hotsix.iAmNotAlone.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.hotsix.iAmNotAlone.login.config.jwt.JwtProperties.*;
import static com.hotsix.iAmNotAlone.login.config.jwt.JwtProperties.SECRET;

@Transactional
@Service
@RequiredArgsConstructor
@Setter(value = AccessLevel.PRIVATE)
@Slf4j
public class JwtService {

    private final MemberRepository memberRepository;

    private String extractUsername(Authentication authentication) {
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        return userDetails.getMember().getEmail();
    }

    public String createAccessToken(String email) {
        log.info("어세스 토큰 생성");
        String token = JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME))
                .withClaim("email", email)
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
        memberRepository.findByEmail(email)
                .ifPresentOrElse(
                        member -> member.updateRefreshToken(refreshToken),
                        () -> new Exception("회원이 없습니다")
                );
    }


    public void destroyRefreshToken(String email) {
        memberRepository.findByEmail(email)
                .ifPresentOrElse(
                        member -> member.destroyRefreshToken(),
                        () -> new Exception("회원이 없습니다")
                );
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);


        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
        tokenMap.put(REFRESH_TOKEN_SUBJECT, refreshToken);

    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);


        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
    }


    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(ACCESS_TOKEN_SUBJECT)).filter(

                accessToken -> accessToken.startsWith(TOKEN_PREFIX)

        ).map(accessToken -> accessToken.replace(TOKEN_PREFIX, ""));
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(REFRESH_TOKEN_SUBJECT)).filter(

                refreshToken -> refreshToken.startsWith(TOKEN_PREFIX)

        ).map(refreshToken -> refreshToken.replace(TOKEN_PREFIX, ""));
    }

    public Optional<String> extractUsername(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(SECRET)).build().verify(accessToken).getClaim("email").asString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(ACCESS_TOKEN_SUBJECT, accessToken);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(REFRESH_TOKEN_SUBJECT, refreshToken);
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(SECRET)).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

