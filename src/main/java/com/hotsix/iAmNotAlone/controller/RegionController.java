package com.hotsix.iAmNotAlone.controller;

import com.hotsix.iAmNotAlone.domain.Region;
import com.hotsix.iAmNotAlone.service.RegionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping("api/regions")
    public List<Region> getRegions() {
        return regionService.findAll();
    }
}
