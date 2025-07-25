package com.common.develop;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CommonJsonUtil {

    // JSON 문자열을 JsonElement로 변환하는 메서드
    public static JsonElement parseJsonString(String jsonString) {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(jsonString);  // JsonObject, JsonArray, JsonPrimitive 모두 처리 가능
    }

    // JSON 문자열을 JsonObject로 변환하는 메서드
    public static JsonObject parseJsonObject(String jsonString) {
        JsonElement jsonElement = parseJsonString(jsonString);
        if (jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
        }
        throw new IllegalArgumentException("Provided string is not a valid JSON Object.");
    }

    // JSON 문자열을 JsonArray로 변환하는 메서드
    public static JsonArray parseJsonArray(String jsonString) {
        JsonElement jsonElement = parseJsonString(jsonString);
        if (jsonElement.isJsonArray()) {
            return jsonElement.getAsJsonArray();
        }
        throw new IllegalArgumentException("Provided string is not a valid JSON Array.");
    }

    // JSON 문자열을 JsonPrimitive로 변환하는 메서드
    public static String parseJsonPrimitive(String jsonString) {
        JsonElement jsonElement = parseJsonString(jsonString);
        if (jsonElement.isJsonPrimitive()) {
            return jsonElement.getAsString();
        }
        throw new IllegalArgumentException("Provided string is not a valid JSON Primitive.");
    }

    // JsonObject에서 특정 필드값을 String으로 추출하는 메서드
    public static String getStringFromJsonObject(JsonObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            return jsonObject.get(key).getAsString();
        }
        throw new IllegalArgumentException("Key '" + key + "' does not exist in the JSON object.");
    }

    // JsonArray에서 인덱스로 특정 JsonObject를 추출하는 메서드
    public static JsonObject getJsonObjectFromJsonArray(JsonArray jsonArray, int index) {
        if (index >= 0 && index < jsonArray.size()) {
            return jsonArray.get(index).getAsJsonObject();
        }
        throw new IllegalArgumentException("Index " + index + " is out of bounds for the JSON array.");
    }
}
