package com.common.develop;

import com.fr.third.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;


public class CommonPropertiesUtil {
    private static Common commonn;

    public Common getCommonn() {
        return commonn;
    }

    public void setCommonn(Common commonn) {
        this.commonn = commonn;
    }


    public static String getPropertyValue(String propertyFilePath, String propertyKey, Class<?> clazz) throws IOException {

        if (commonn == null) {
            commonn = new Common();
        }

        // 프로퍼티 파일을 읽어서 JSON 객체로 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> parsedJson = objectMapper.readValue(commonn.getPropertyJson(propertyFilePath, clazz), Map.class);

        // Map에서 필요한 키값을 추출하여 반환
        if (parsedJson.containsKey(propertyKey)) {
            return parsedJson.get(propertyKey).toString();
        } else {
            throw new IllegalArgumentException("Property key " + propertyKey + " not found in " + propertyFilePath);
        }
    }


}