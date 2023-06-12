package com.hotsix.iAmNotAlone.domain.region.repository;

import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

}
