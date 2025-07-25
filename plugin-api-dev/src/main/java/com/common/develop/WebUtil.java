package com.common.develop;

import com.fr.decision.webservice.utils.WebServiceUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.third.org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class WebUtil {
    public static String parseWebPageResourceSafe(String path, Map<String, Object> paramMap, String svc) throws Exception {
        if (StringUtils.isNotEmpty(svc) && !"uplus".equals(svc)) {
            path = "/" + svc + path;
        }

        paramMap.put("timestamp", System.currentTimeMillis());

        return WebServiceUtils.parseWebPageResourceSafe(path, paramMap);
    }

    public static String parseWebPageResource(String path, Map<String, Object> paramMap, String svc) throws Exception {
        if (StringUtils.isNotEmpty(svc) && !"uplus".equals(svc)) {
            path = "/" + svc + path;
        }

        paramMap.put("timestamp", System.currentTimeMillis());

        return WebServiceUtils.parseWebPageResource(path, paramMap);
    }

    public  Map<String, Object> parseQueryString(String queryString) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<>();
        FineLoggerFactory.getLogger().error("request queryString " + queryString);
        if (queryString == null || queryString.isEmpty()) {
            return result; // 빈 Map 반환
        }

        // 쿼리 문자열을 & 기준으로 분리
        String[] pairs = queryString.split("&");

        for (String pair : pairs) {
            // = 기준으로 key와 value 분리
            String[] keyValue = pair.split("=", 2); // 2로 제한해 key와 value만 분리
            String key = URLDecoder.decode(keyValue[0], "UTF-8"); // key 디코딩
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], "UTF-8") : ""; // value 디코딩 (없으면 빈 문자열)
            result.put(key, value);
        }

        return result;
    }
}
