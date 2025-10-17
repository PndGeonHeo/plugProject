package com.mes.service;

import com.fr.log.FineLoggerFactory;
import com.fr.log.FineLoggerProvider;
import com.fr.stable.StringUtils;
import com.mes.common.Common;
import com.mes.common.JdbcUtils;
import com.mes.response.MesResponse;
import okhttp3.*;

import java.util.Properties;

import static com.mes.common.CommonJsonUtil.stringToObject;

public abstract class ApiHelper {
    final Properties props;
    final static String SQL_FILENAME = "sqlmap.xml";
    final static FineLoggerProvider log = FineLoggerFactory.getLogger();

    protected ApiHelper() {
        this.props = Common.getProperties("conf/app.properties");;
    }

    // jdbc 연결
    public JdbcUtils getJdbcConnection() {
        try {
            return new JdbcUtils(SQL_FILENAME);
        } catch (Exception e) {
            throw new RuntimeException("JDBC 연결 실패", e);
        }
    }

    public Object callPostAPI(String jsonStr, String url) {
        System.out.println("jsonStr: " + jsonStr);
        System.out.println("url: " + url);
        OkHttpClient client = new OkHttpClient();

        // RequestBody 생성
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);

        // 요청 값
        Request request = new Request.Builder()
                .headers(getHeaders())
                .url(url)
                .post(body)
                .build();

        // 호출 및 응답
        try {
            Response response = client.newCall(request).execute();
            String bodyStr = response.body().string();
            log.info("응답 코드: {}, 응답 바디 : {}", response.code(), bodyStr);

            // 추후 해당 내용 지우기
            bodyStr = "{"
                    + "\"result\": {"
                    + "\"code\": \"2000\","
                    + "\"status\": \"200\","
                    + "\"type\": \"IF-200\","
                    + "\"desc\": \"success.\""
                    + "}"
                    + "}";
            if (!StringUtils.isEmpty(bodyStr)) {
                return stringToObject(bodyStr, MesResponse.class);
            }

        } catch (Exception e) {
            log.error("HTTP ERROR CODE => {}", e.getMessage());
        }

        return null;
    }

    private Headers getHeaders() {
        return new Headers.Builder()
                .add("Content-type", Common.getProperties("conf/app.properties").getProperty("header.content-type"))
                .add("Accept", Common.getProperties("conf/app.properties").getProperty("header.accept"))
                .add("x-certkey", Common.getProperties("conf/app.properties").getProperty("header.x-certkey"))
                .add("x-uuid", Common.getProperties("conf/app.properties").getProperty("header.x-uuid"))
                .add("x-commandtype", Common.getProperties("conf/app.properties").getProperty("header.x-commandtype"))
                .build();
    }
}
