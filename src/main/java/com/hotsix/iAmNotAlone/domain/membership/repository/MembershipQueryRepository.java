package com.hotsix.iAmNotAlone.domain.membership.repository;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MembershipQueryRepository {

    private final EntityManager em;

    public Membership findByIdMembership(Long userId){
        return em.createQuery(
                "select m from Membership m"+
                        " join fetch m.region r" +
                        " where m.id = :userId",Membership.class)
                .setParameter("userId",userId)
                .getSingleResult();
    }
}
