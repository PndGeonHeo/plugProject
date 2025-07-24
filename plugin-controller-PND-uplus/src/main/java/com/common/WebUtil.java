package com.common;

import com.fr.decision.webservice.utils.WebServiceUtils;
import com.fr.third.org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class WebUtil {
    public static String parseWebPageResourceSafe(String path, Map<String, Object> paramMap, String svc) throws Exception {
        if (StringUtils.isNotEmpty(svc) && !"uplus".equals(svc)) {
            path = "/" + svc + path;
        }

        if (paramMap == null) {
            paramMap = new HashMap<>();
        }

        paramMap.put("timestamp", System.currentTimeMillis());

        return WebServiceUtils.parseWebPageResourceSafe(path, paramMap);
    }

    public static String parseWebPageResource(String path, Map<String, Object> paramMap, String svc) throws Exception {
        if (StringUtils.isNotEmpty(svc) && !"uplus".equals(svc)) {
            path = "/" + svc + path;
        }

        if (paramMap == null) {
            paramMap = new HashMap<>();
        }

        paramMap.put("timestamp", System.currentTimeMillis());

        return WebServiceUtils.parseWebPageResource(path, paramMap);
    }
}
