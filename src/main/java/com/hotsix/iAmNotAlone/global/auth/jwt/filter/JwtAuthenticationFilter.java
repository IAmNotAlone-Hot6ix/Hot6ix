package com.hotsix.iAmNotAlone.global.auth.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.global.auth.PrincipalDetails;
import com.hotsix.iAmNotAlone.global.auth.jwt.JwtService;
import com.hotsix.iAmNotAlone.global.auth.signin.model.form.LoginRequestForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final MembershipRepository membershipRepository;
    private final JwtService jwtService;

    //인증 체크
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("JwtAuthenticationFilter : 로그인 시도중");

        ObjectMapper om = new ObjectMapper();
        LoginRequestForm loginRequestForm = null;

        try {
            loginRequestForm = om.readValue(request.getInputStream(), LoginRequestForm.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("loginForm", loginRequestForm);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestForm.getEmail(), loginRequestForm.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        return authentication;
    }

    // 로그인 성공시 메서드를 탐
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        ObjectMapper om = new ObjectMapper();
        log.info("로그인 성공");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        String email = principalDetails.getMember().getEmail();
        Long id = principalDetails.getMember().getId();
        String accessToken = jwtService.createAccessToken(id, email);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.updateRefreshToken(email, refreshToken);

        Map<String, String> accessRefreshMap = jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        String jsonAccessRefreshMap = om.writeValueAsString(accessRefreshMap);

        log.info("로그인에 성공합니다. email: {}", email);
        log.info("AccessToken 을 발급합니다. AccessToken: {}", accessToken);
        log.info("RefreshToken 을 발급합니다. RefreshToken: {}", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(jsonAccessRefreshMap);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        String errorMessage = null;

        LoginRequestForm loginRequestForm = (LoginRequestForm) request.getAttribute("loginForm");
        String email = loginRequestForm.getEmail();

        Optional<Membership> emailOptional = membershipRepository.findByEmail(email);
        response.setCharacterEncoding("utf-8");

        if (!emailOptional.isPresent()) {
            errorMessage = "가입된 회원이 존재하지 않습니다. 회원가입을 진행하여 주세요";
        } else if (failed instanceof BadCredentialsException) {
            errorMessage = "아이디나 비밀번호가 일치하지 않습니다. 다시 입력해 주세요";
        }

        if (errorMessage != null) {
            response.getWriter().write(errorMessage);
        }
    }
}

