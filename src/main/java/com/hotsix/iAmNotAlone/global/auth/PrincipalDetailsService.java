package com.hotsix.iAmNotAlone.global.auth;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MembershipRepository membershipRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Membership> optionalMember = membershipRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            Membership member = optionalMember.get();
            return new PrincipalDetails(member);
        } else {
            throw new UsernameNotFoundException("이메일이 없습니다.");
        }
    }

}
