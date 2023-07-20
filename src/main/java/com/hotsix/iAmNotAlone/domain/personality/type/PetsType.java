package com.hotsix.iAmNotAlone.domain.personality.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PetsType {

    HAS_PET(0, "반려동물 있음"),
    NO_PET(1, "반려동물 없음"),
    PET_INDIFFERENT(2, "상관없음");

    private final int id;
    private final String value;


    // ID를 이용하여 해당 PetsType 을 반환하는 메서드
    public static String getById(int id) {
        for (PetsType type : values()) {
            if (type.id == id) {
                return type.value;
            }
        }
        throw new IllegalArgumentException("Invalid PetsType id: " + id);
    }
}
