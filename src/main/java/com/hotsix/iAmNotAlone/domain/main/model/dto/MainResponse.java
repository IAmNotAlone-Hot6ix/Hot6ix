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

    private Long region_id;


    public static MainResponse of(List<RegionMainDto> regionList, Long region_id) {
        return MainResponse.builder()
            .regionMainDtoList(regionList)
            .region_id(region_id)
            .build();
    }
}
