package com.common.develop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * DataFormatConverterGson 클래스
 *
 * - 다른 Java 파일에서 new DataFormatConverterGson() 으로 인스턴스를 생성한 뒤,
 *   parseOrConvert(), toPrettyJsonString(), toCompactJsonString() 등을 바로 호출할 수 있습니다.
 * - 내부적으로 사용하는 Gson 인스턴스는 static final로 선언되어, 애플리케이션 당 한 번만 생성됩니다.
 */
public class DataFormatConvertGson {

    // Pretty-Printing 전용 Gson (들여쓰기 포함)
    private static final Gson prettyGson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    // Compact 출력 전용 Gson (들여쓰기 없음)
    private static final Gson compactGson = new Gson();

    /**
     * 기본 public 생성자
     * 다른 클래스에서는 new DataFormatConverterGson() 으로 인스턴스를 생성하여 사용합니다.
     */
    public DataFormatConvertGson() {
        // 별도의 초기화 로직이 필요 없으므로 비워둡니다.
    }

    /**
     * 입력 문자열이 JSON인지 판별한 뒤 JsonElement를 반환하는 인스턴스 메서드
     *
     * @param input 입력 문자열 (예: "{\"user\":\"aaaa\"}" 또는 "user=aaaa&age=20")
     * @return JsonElement (파싱된 JSON 혹은 쿼리 스트링을 변환한 JsonObject)
     * @throws IllegalArgumentException 입력이 null이거나 빈 문자열일 때
     */
    public JsonElement parseOrConvert(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("입력 문자열이 null이거나 빈 값입니다.");
        }

        String trimmed = input.trim();
        try {
            // 구버전 Gson: new JsonParser().parse(...)
            JsonParser parser = new JsonParser();
            JsonElement parsed = parser.parse(trimmed);
            return parsed;
        } catch (JsonSyntaxException e) {
            // JSON 구문 오류가 발생하면, 쿼리 스트링으로 취급하고 아래 로직으로 넘어갑니다.
        }

        // 쿼리 스트링 형태 ("key=value&key2=value2") → JsonObject 변환
        JsonObject resultObj = new JsonObject();
        String[] pairs = trimmed.split("&");
        for (String pair : pairs) {
            if (pair.isEmpty()) {
                continue;
            }
            int idx = pair.indexOf('=');
            if (idx < 0) {
                // '=' 문자가 없는 경우, 키만 있고 값은 빈 문자열로 간주
                String key = decode(pair);
                resultObj.addProperty(key, "");
            } else {
                String rawKey = pair.substring(0, idx);
                String rawValue = pair.substring(idx + 1);
                String key = decode(rawKey);
                String value = decode(rawValue);
                resultObj.addProperty(key, value);
            }
        }
        return resultObj;
    }

    /**
     * JsonElement를 Pretty-Print 형태의 문자열로 반환하는 인스턴스 메서드
     *
     * @param element Gson JsonElement
     * @return 들여쓰기(Pretty - Printing)된 JSON 문자열
     */
    public String toPrettyJsonString(JsonElement element) {
        return prettyGson.toJson(element);
    }

    /**
     * JsonElement를 Compact 형태의 문자열로 반환하는 인스턴스 메서드
     *
     * @param element Gson JsonElement
     * @return 최소 공백(Compact) JSON 문자열
     */
    public String toCompactJsonString(JsonElement element) {
        return compactGson.toJson(element);
    }

    /**
     * URL 인코딩된 문자열을 UTF-8 기반으로 디코딩
     *
     * @param text 디코딩할 문자열
     * @return 디코딩된 문자열 (예: "user%20name" → "user name")
     */
    private String decode(String text) {
        try {
            return URLDecoder.decode(text, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // UTF-8은 JVM이 기본 지원하므로 거의 여기로 오지 않음
            return text;
        }
    }
}