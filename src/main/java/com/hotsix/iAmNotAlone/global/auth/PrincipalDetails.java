package com.hotsix.iAmNotAlone.global.auth;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@Slf4j
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Membership member;
    private Map<String, Object> attributes;

    // 일반 로그인
    public PrincipalDetails(Membership member) {
        this.member = member;
    }

    // OAuth 로그인
    public PrincipalDetails(Membership member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        log.info("PrincipalDetails{}" + member.getRole());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getRole().toString()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return attributes.get("email").toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


}
