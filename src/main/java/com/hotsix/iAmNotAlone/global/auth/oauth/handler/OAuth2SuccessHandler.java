package com.hotsix.iAmNotAlone.global.auth.oauth.handler;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.global.auth.PrincipalDetails;
import com.hotsix.iAmNotAlone.global.auth.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Membership member = principal.getMember();
        String additionalInputUri = "";
        Long id = member.getId();
        String email = member.getEmail();
        String accessToken = jwtService.createAccessToken(id, email);
        String refreshToken = jwtService.createRefreshToken();

        if (member.getRegion().getId()==26){
            additionalInputUri = "https://iamnotalone.vercel.app/socialsignup/"+member.getId();
            getRedirectStrategy().sendRedirect(request, response, additionalInputUri);
        }else {
            additionalInputUri = "https://iamnotalone.vercel.app/";
            getRedirectStrategy().sendRedirect(request, response, additionalInputUri);
            addCookie(response,"accessToken",accessToken);
            addCookie(response,"refreshToken",refreshToken);
            addCookie(response,"accountId",member.getId());
            jwtService.updateRefreshToken(email,refreshToken);
        }

    }

    private static void addCookie(HttpServletResponse response,String name,String value,boolean httpOnly){
        value = URLEncoder.encode(value, StandardCharsets.UTF_8);
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(httpOnly);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    private static void addCookie(HttpServletResponse response,String name,String value){
        addCookie(response,name,value,true);
    }
    private static void addCookie(HttpServletResponse response,String name,Long value){
        addCookie(response,name,String.valueOf(value),true);
    }
}
