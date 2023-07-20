package com.hotsix.iAmNotAlone.domain.personality.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SmokingType {

    SMOKING(0, "흡연"),
    NON_SMOKING(1, "비흡연"),
    DOESNT_MATTER(2, "상관없음");

    private final int id;
    private final String value;


    // ID를 이용하여 해당 SmokingType 을 반환하는 메서드
    public static String getById(int id) {
        for (SmokingType type : values()) {
            if (type.id == id) {
                return type.value;
            }
        }
        throw new IllegalArgumentException("Invalid SmokingType id: " + id);
    }
}
