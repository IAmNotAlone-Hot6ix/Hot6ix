package com.hotsix.iAmNotAlone.service;

import com.hotsix.iAmNotAlone.domain.Region;
import com.hotsix.iAmNotAlone.repository.RegionRepository;
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
