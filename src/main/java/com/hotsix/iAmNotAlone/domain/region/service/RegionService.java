package com.hotsix.iAmNotAlone.domain.region.service;

import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.domain.region.repository.RegionRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {

    private final RegionRepository regionRepository;

    public List<Region> findAll() {
        return regionRepository.findAll();
    }
}
