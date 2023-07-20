package com.hotsix.iAmNotAlone.domain.personality.repository;

import com.hotsix.iAmNotAlone.domain.personality.entity.Personality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalityRepository extends JpaRepository<Personality, Long> {

}
