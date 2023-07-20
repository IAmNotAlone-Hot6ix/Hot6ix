package com.hotsix.iAmNotAlone.domain.personality.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActiveTimeType {

    EARLY_MORNING(0, "24~06"),
    MORNING(1, "06~12"),
    AFTERNOON(2, "12~18"),
    EVENING(3, "18~24");

    private final int id;
    private final String value;


    // ID를 이용하여 해당 ActiveTimeType 을 반환하는 메서드
    public static String getById(int id) {
        for (ActiveTimeType type : values()) {
            if (type.id == id) {
                return type.value;
            }
        }
        throw new IllegalArgumentException("Invalid ActiveTimeType id: " + id);
    }
}
