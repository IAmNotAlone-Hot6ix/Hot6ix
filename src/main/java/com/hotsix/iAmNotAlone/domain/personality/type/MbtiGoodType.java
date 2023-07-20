package com.hotsix.iAmNotAlone.domain.personality.type;

import java.util.Arrays;
import java.util.List;

public enum MbtiGoodType {

    INFP(Arrays.asList("ENFJ", "ENTJ")),
    ENFP(Arrays.asList("INFJ", "INTJ")),
    INFJ(Arrays.asList("ENFP", "ENTP")),
    ENFJ(Arrays.asList("INFP", "ISFP")),
    INTJ(Arrays.asList("ENFP", "ENTP")),
    ENTJ(Arrays.asList("INFP", "INTP")),
    INTP(Arrays.asList("ENTJ", "ESTJ")),
    ENTP(Arrays.asList("INFJ", "INTJ")),
    ISFP(Arrays.asList("ENFJ", "ESFJ", "ESTJ")),
    ESFP(Arrays.asList("ISFJ", "ISTJ")),
    ISTP(Arrays.asList("ESFJ", "ESTJ")),
    ESTP(Arrays.asList("ISFJ", "ISTJ")),
    ISFJ(Arrays.asList("ESFP", "ESTP")),
    ESFJ(Arrays.asList("ISFP", "ISTP")),
    ISTJ(Arrays.asList("ESFP", "ESTP")),
    ESTJ(Arrays.asList("ISFP", "ISTP"));

    private final List<String> matches;

    MbtiGoodType(List<String> matches) {
        this.matches = matches;
    }

    public List<String> getMatches() {
        return matches;
    }
}
