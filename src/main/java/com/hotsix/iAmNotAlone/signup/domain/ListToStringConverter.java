package com.hotsix.iAmNotAlone.signup.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

@Convert
public class ListToStringConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        String personality = "";
        for (int i = 0; i < attribute.size()-1; i++) {
            personality += attribute.get(i) + ",";
        }
        personality += attribute.get(attribute.size() - 1);
        return personality;
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        String[] split = dbData.split(",");
        List<String> personalities = new ArrayList<>();
        for (String p : split) {
            personalities.add(p);
        }
        return personalities;
    }

}
