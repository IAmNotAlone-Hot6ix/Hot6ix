package com.hotsix.iAmNotAlone.domain.membership.repository;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Optional<Membership> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Membership> findByRefreshToken(String refreshToken);
}
