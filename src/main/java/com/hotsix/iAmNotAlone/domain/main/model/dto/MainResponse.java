package com.hotsix.iAmNotAlone.domain.main.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MainResponse {

    private List<RegionMainDto> regionMainDtoList;

    private Long regionId;


    public static MainResponse of(List<RegionMainDto> regionList, Long regionId) {
        return MainResponse.builder()
            .regionMainDtoList(regionList)
            .regionId(regionId)
            .build();
    }
}
