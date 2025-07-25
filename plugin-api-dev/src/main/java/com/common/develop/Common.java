package com.common.develop;

import com.controller.develop.ftp;
import com.fr.decision.service.context.ServiceContext;
import com.fr.decision.webservice.annotation.LoginStatusChecker;
import com.fr.log.FineLoggerFactory;
import com.fr.third.fasterxml.jackson.databind.ObjectMapper;
import com.fr.third.springframework.beans.factory.annotation.Autowired;
import com.fr.third.springframework.stereotype.Controller;
import com.fr.third.springframework.web.bind.annotation.RequestMapping;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping(value = "/{svc}")
@LoginStatusChecker(required = false)
public class Common {
    private static final String SQL_FILENAME = "sqlmap.xml";
    private static Common commonInstacne = null;
    @Autowired
    private ServiceContext serviceContext;

    public Common() {
    }

    // Common 객체 반환 (싱글톤 패턴 사용)
    public static Common getInstance() {
        if (commonInstacne == null) {
            synchronized (Common.class) {
                if (commonInstacne == null) {
                    commonInstacne = new Common(); // 객체가 없으면 생성
                }
            }
        }
        return commonInstacne;
    }

    public void setServiceContext(ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    public Common(ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }


    /*
        get properties
        로컬 환경: 프로젝트폴더\build\classes 하위
    */
    public static Properties getProperties(String path) {
        Properties props = new Properties();

        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) cl = ClassLoader.getSystemClassLoader();

            URL url = cl.getResource(path);

            if (url == null) {
                throw new Exception("properties not exist. path=" + path);
            }

            String filepath = url.getFile();
            props.load(new InputStreamReader(new FileInputStream(filepath)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return props;
    }

    /*
        get property in path
    */
    public static String getProperty(String path, String name) {
        String result = "";

        try {
            Properties props = getProperties(path);
            result = props.getProperty(name);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getPropertyJson(String path){
        Properties properties = new Properties();
        ClassLoader classLoader = ftp.class.getClassLoader();
        String returnString = "";

        Map<String, Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream input = classLoader.getResourceAsStream(path)) {
            if (input == null) {
                map.put("message", "Properties 파일을 찾을 수 없습니다.");
                map.put("code", 404);
                returnString = objectMapper.writeValueAsString(map);
                return returnString;
            }

            // Load the properties file
            properties.load(input);


            for (String name : properties.stringPropertyNames()) {
                map.put(name, properties.getProperty(name));
            }

            returnString = objectMapper.writeValueAsString(map);

            return  returnString;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnString;
    }

    public static String getPropertyJson(String path,Class<?> clz){
        Properties properties = new Properties();
        ClassLoader classLoader = clz.getClassLoader();

        String returnString = "";

        Map<String, Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream input = classLoader.getResourceAsStream(path)) {
            InputStreamReader reader = new InputStreamReader(input , StandardCharsets.UTF_8);
            if (input == null) {
                map.put("message", "Properties 파일을 찾을 수 없습니다.");
                map.put("code", 404);
                returnString = objectMapper.writeValueAsString(map);
                return returnString;
            }

            // Load the properties file
            properties.load(reader);


            for (String name : properties.stringPropertyNames()) {
                map.put(name, properties.getProperty(name));
            }

            returnString = objectMapper.writeValueAsString(map);

            return  returnString;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnString;
    }

    public static String getPropertyJsonTree(String path, Class<?> clz) {
        Properties properties = new Properties();
        ClassLoader classLoader = clz.getClassLoader();
        Map<String, Object> rootMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String returnString = "";

        try (InputStream input = classLoader.getResourceAsStream(path);
             InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {

            if (input == null) {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("message", "Properties 파일을 찾을 수 없습니다.");
                errorMap.put("code", 404);
                return objectMapper.writeValueAsString(errorMap); // 한 줄로 출력
            }

            // Load properties
            properties.load(reader);

            // Parse properties into nested JSON structure
            for (String key : properties.stringPropertyNames()) {
                String[] parts = key.split("\\."); // Split key by '.'
                Map<String, Object> currentMap = rootMap;

                for (int i = 0; i < parts.length; i++) {
                    String part = parts[i];

                    if (i == parts.length - 1) {
                        // 마지막 요소라면 값을 추가
                        currentMap.put(part, properties.getProperty(key));
                    } else {
                        // 중첩된 Map 생성 또는 탐색
                        currentMap = (Map<String, Object>) currentMap.computeIfAbsent(part, k -> new HashMap<>());
                    }
                }
            }

            // Convert the resulting map to a single-line JSON string
            returnString = objectMapper.writeValueAsString(rootMap);

        } catch (IOException e) {
            e.printStackTrace();
            try {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("message", "파일 읽기 중 오류가 발생했습니다.");
                errorMap.put("code", 500);
                returnString = objectMapper.writeValueAsString(errorMap); // 한 줄로 출력
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return returnString;
    }


    public static String getMapValue(Map<String,Object> map,String mapKey) throws IOException {
        // Map에서 필요한 키값을 추출하여 반환
        if (map.containsKey(mapKey)) {
            return map.get(mapKey).toString();
        } else {
            throw new IllegalArgumentException("Property key " + mapKey );
        }
    }
}