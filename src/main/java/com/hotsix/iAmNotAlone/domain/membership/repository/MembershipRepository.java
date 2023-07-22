package com.hotsix.iAmNotAlone.domain.membership.repository;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Optional<Membership> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Membership> findByRefreshToken(String refreshToken);

    @Query(value = "select m from Membership m" +
        " join fetch m.region r" +
        " where m.id = :userId")
    Optional<Membership> findByIdMembership(@Param("userId") Long userId);


    List<Membership> findAllByIdNotAndRegionIdAndGenderAndPersonalityIdNotNull(
        @Param("memberId") Long memberId, @Param("regionId") Long regionId,
        @Param("gender") int gender);

}
