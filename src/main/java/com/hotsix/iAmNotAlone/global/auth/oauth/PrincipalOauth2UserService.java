package com.hotsix.iAmNotAlone.global.auth.oauth;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.global.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.hotsix.iAmNotAlone.global.auth.common.Role.USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final MembershipRepository membershipRepository;

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

        Region region = Region.builder().id(26L).build();

        Membership membership;
        if (memberOptional.isEmpty()) {
            membership = Membership.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .imgPath(imgPath)
                    .role(USER)
                    .region(region)
                    .build();
            membershipRepository.save(membership);
        } else {
            membership = memberOptional.get();
        }

        return new PrincipalDetails(membership, attributes);
    }
}
