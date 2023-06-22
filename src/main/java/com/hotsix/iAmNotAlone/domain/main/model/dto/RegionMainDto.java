package com.hotsix.iAmNotAlone.domain.main.model.dto;

import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegionMainDto {

    Long regionId;
    String sido;
    String sigg;

    public static RegionMainDto from(Region region) {
        return RegionMainDto.builder()
            .regionId(region.getId())
            .sido(region.getSido())
            .sigg(region.getSigg())
            .build();
    }
}
