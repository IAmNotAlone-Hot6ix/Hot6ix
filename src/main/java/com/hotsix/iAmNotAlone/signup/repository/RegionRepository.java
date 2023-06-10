package com.hotsix.iAmNotAlone.signup.repository;

import com.hotsix.iAmNotAlone.signup.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

}
