package com.hotsix.iAmNotAlone.global.auth.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.global.auth.PrincipalDetails;
import com.hotsix.iAmNotAlone.global.auth.jwt.JwtService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotsix.iAmNotAlone.global.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.hotsix.iAmNotAlone.global.auth.jwt.JwtProperties.*;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MembershipRepository membershipRepository;
    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MembershipRepository memberRepository, JwtService jwtService) {
        super(authenticationManager);
        this.membershipRepository = memberRepository;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String authorizationHeader = request.getHeader(ACCESS_TOKEN_SUBJECT);
        String refreshTokenHeader = request.getHeader(REFRESH_TOKEN_SUBJECT);
        System.out.println(authorizationHeader);
        System.out.println(refreshTokenHeader);

        String accessTokenValid = jwtService.extractAccessToken(request)
                .filter(token -> jwtService.isTokenValid(token))
                .orElse(null);

        if (authorizationHeader == null || (authorizationHeader != null && refreshTokenHeader != null)) {
            filterChain.doFilter(request, response);
            return;
        } else if (authorizationHeader.equals("") || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
            // 토큰값이 없거나 정상적이지 않다면 400 오류
            log.info("CustomAuthorizationFilter : JWT Token이 존재하지 않습니다.");
            response.setStatus(SC_BAD_REQUEST);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            ErrorResponse errorResponse = new ErrorResponse(SC_BAD_REQUEST, "유효하지 않은 Refresh Token 입니다.");
            response.getWriter().write(errorResponse.toString());
            throw new RuntimeException("유효하지 않은 Refresh Token 입니다.");
        }
        log.info("doFilterInternal 인증이필요한 필터 타게됨");

        System.out.println(accessTokenValid);
        //권한없음 신호 프론트에게 응답
        //refreshToken 달라고 요청
        if (accessTokenValid == null) {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            ErrorResponse errorResponse = new ErrorResponse(SC_UNAUTHORIZED, "Access Token이 만료되었습니다.");
            new ObjectMapper().writeValue(response.getWriter(), errorResponse);
            return;
        } else {
            jwtService.extractUsername(accessTokenValid).ifPresent(
                    username -> membershipRepository.findByEmail(username).ifPresent(
                            member -> saveAuthentication(member)
                    )
            );
        }
        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(Membership member) {
        PrincipalDetails principalDetails = new PrincipalDetails(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principalDetails,
                null,
                principalDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
