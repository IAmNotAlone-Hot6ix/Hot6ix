package com.hotsix.iAmNotAlone.global.auth.jwt.filter;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.global.auth.PrincipalDetails;
import com.hotsix.iAmNotAlone.global.auth.jwt.JwtProperties;
import com.hotsix.iAmNotAlone.global.auth.jwt.JwtService;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MembershipRepository membershipRepository;
    private final JwtService jwtService;
    private final List<String> ALLOWEDPREFIXES = Arrays.asList("/login", "/signup", "/board/list");


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
        MembershipRepository membershipRepository, JwtService jwtService) {
        super(authenticationManager);
        this.membershipRepository = membershipRepository;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain)
        throws IOException, ServletException {

        String ALLOWED_PREFIX_REGEX = "^/email.*|/swagger-ui.*|/v3/api-docs.*|/swagger-resources.*|/members.*|/region.*$";

        // if 문에 걸린 url 요청 그냥 return
        if (ALLOWEDPREFIXES.contains(request.getRequestURI())
            || Pattern.matches(ALLOWED_PREFIX_REGEX, request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        /*
            엑세스 토큰과 리프레쉬 토큰을 같이 보냈을 경우
            리프레쉬 토큰 유효성 검사
         */
        String refreshToken = jwtService.extractRefreshToken(request)
            .filter(token -> jwtService.isTokenValid(token))
            .orElse(null);

        if (refreshToken != null) {
            refreshToken = JwtProperties.TOKEN_PREFIX + refreshToken;
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        // 엑세스 토큰 유효성 검사
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    /**
     * AccessToken 유효성 검사
     */
    private void checkAccessTokenAndAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        Optional<String> optionalAccessToken = jwtService.extractAccessToken(request);

        // 토큰이 비어있는지 확인
        if (optionalAccessToken.isEmpty()) {
            sendUnauthorizedError(response, "로그인 후 사용 가능합니다.");
            return;
        }

        // 토큰 유효성 검사
        String accessToken = optionalAccessToken.get();
        if (!jwtService.isTokenValid(accessToken)) {
            sendUnauthorizedError(response, "유효하지 않은 토큰입니다.");
            return;
        }

        // 토큰 소유자 확인
        authenticateUser(accessToken);
        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(Membership member) {
        PrincipalDetails principalDetails = new PrincipalDetails(member);
        Authentication authentication =
            new UsernamePasswordAuthenticationToken(principalDetails, null,
                principalDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * response 에러 전달
     */
    private void sendUnauthorizedError(HttpServletResponse response, String message)
        throws IOException {
        log.error(message);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }

    /**
     * 토큰 소유자 확인
     */
    private void authenticateUser(String accessToken) {
        jwtService.extractUsername(accessToken)
            .ifPresent(username ->
                membershipRepository.findByEmail(username)
                    .ifPresent(this::saveAuthentication)
            );
    }

    /**
     * RefreshToken 확인 후 AccessToken 으로 대체
     */
    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response,
        String refreshToken) {
        log.info("checkRefreshTokenAndReIssueAccessToken 진입");
        membershipRepository.findByRefreshToken(refreshToken)
            .ifPresent(member -> jwtService.sendAccessToken(response,
                jwtService.createAccessToken(member.getEmail()))
            );
    }

}
