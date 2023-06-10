package com.hotsix.iAmNotAlone.signup.repository;

import com.hotsix.iAmNotAlone.signup.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

}
