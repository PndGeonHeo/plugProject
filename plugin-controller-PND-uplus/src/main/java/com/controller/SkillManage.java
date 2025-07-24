package com.controller;

import com.common.JdbcUtils;
import com.fr.decision.authority.data.User;
import com.fr.decision.service.authority.DecisionUserServiceProvider;
import com.fr.decision.service.context.ServiceContext;
import com.fr.decision.webservice.annotation.LoginStatusChecker;
import com.fr.log.FineLoggerFactory;
import com.fr.third.fasterxml.jackson.databind.ObjectMapper;
import com.fr.third.org.apache.http.HttpResponse;
import com.fr.third.org.apache.http.client.HttpClient;
import com.fr.third.org.apache.http.client.ResponseHandler;
import com.fr.third.org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import com.fr.third.org.apache.http.client.methods.HttpPost;
import com.fr.third.org.apache.http.client.methods.HttpPut;
import com.fr.third.org.apache.http.entity.ContentType;
import com.fr.third.org.apache.http.entity.StringEntity;
import com.fr.third.org.apache.http.impl.client.BasicResponseHandler;
import com.fr.third.org.apache.http.impl.client.HttpClientBuilder;
import com.fr.third.springframework.beans.factory.annotation.Qualifier;
import com.fr.third.springframework.stereotype.Controller;
import com.fr.third.springframework.web.bind.annotation.*;
import com.record.SkillManageRecord;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller

@LoginStatusChecker(required = false)
public class SkillManage {

    final static String testUrl = "http://127.0.0.1:8075/webroot/decision/uplus/skill/register/test";

    // 스킬 api
    private static String apiGwSkillRegister = Common.getProperty("conf/app.properties", "api.gw.skill.register");

    final static String response_ex = "\"{\\\"ffa93b72-b4ab-4931-9677-c59f9d77228d\\\":{\\\"total\\\":1,\\\"pageCount\\\":null,\\\"pageNumber\\\":null,\\\"entities\\\":[{\\\"selfUri\\\":\\\"/api/v2/users/ffa93b72-b4ab-4931-9677-c59f9d77228d/routingskills/370f1206-267a-410e-86fd-7dafede30a31\\\",\\\"name\\\":\\\"[dj]VIP\\\",\\\"skillUri\\\":\\\"/api/v2/routing/skills/370f1206-267a-410e-86fd-7dafede30a31\\\",\\\"id\\\":\\\"370f1206-267a-410e-86fd-7dafede30a31\\\",\\\"state\\\":\\\"ACTIVE\\\",\\\"proficiency\\\":3}],\\\"firstUri\\\":null,\\\"lastUri\\\":null,\\\"selfUri\\\":null,\\\"pageSize\\\":null,\\\"nextUri\\\":null,\\\"previousUri\\\":null},\\\"count\\\":2,\\\"c3add500-da54-494b-a4de-2ee8ba6255f1\\\":{\\\"total\\\":1,\\\"pageCount\\\":null,\\\"pageNumber\\\":null,\\\"entities\\\":[{\\\"selfUri\\\":\\\"/api/v2/users/c3add500-da54-494b-a4de-2ee8ba6255f1/routingskills/370f1206-267a-410e-86fd-7dafede30a31\\\",\\\"name\\\":\\\"[dj]VIP\\\",\\\"skillUri\\\":\\\"/api/v2/routing/skills/370f1206-267a-410e-86fd-7dafede30a31\\\",\\\"id\\\":\\\"370f1206-267a-410e-86fd-7dafede30a31\\\",\\\"state\\\":\\\"ACTIVE\\\",\\\"proficiency\\\":3}],\\\"firstUri\\\":null,\\\"lastUri\\\":null,\\\"selfUri\\\":null,\\\"pageSize\\\":null,\\\"nextUri\\\":null,\\\"previousUri\\\":null},\\\"status\\\":200}\"";

    private ServiceContext serviceContext;
    public SkillManage(@Qualifier("decision") ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    @RequestMapping(value="/uplus/skill/manage/{type}/U" ,method = RequestMethod.POST)
    @ResponseBody
    public String skillupdate(HttpServletRequest req, @RequestBody List<SkillManageRecord> params, @PathVariable String type ) throws Exception {
        Map<String,Object> resMap = new HashMap<>();
        try {
            FineLoggerFactory.getLogger().error("=========================== SKILL_UPDATE START ==============");
            FineLoggerFactory.getLogger().error("SKILL_UPDATE PARAMS========" + params);
            User user = serviceContext.getService(DecisionUserServiceProvider.class).getUserByRequestCookie(req);
            String userId = user.getUserName();
            JdbcUtils jdbcUtils = new JdbcUtils();
            //Map<String, Object> paramMap = new Gson().fromJson(params, Map.class);

            //User LiSt
            List<String> userListId = new ArrayList<String>();
            JSONArray jarray = new JSONArray();
            for(int i = 0; i< params.get(0).getUserList().size(); i ++){
                JSONObject arrayObject = new JSONObject();
                arrayObject.put("id",params.get(0).getUserList().get(i).getId());
                jarray.put(arrayObject);
            }


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("operatorId",params.get(0).getOperatorId());
            jsonObject.put("skillCartId",params.get(0).getSkillCartId());
            jsonObject.put("userList",jarray);

            FineLoggerFactory.getLogger().error("SKILL_UPDATE_JSONOBJECT PARAMS========" + jsonObject.toString());
            resMap = this.skillRegister_skill_cart(jsonObject.toString());
            //response Log
            resMapLog(resMap);

            String updateSql = "";
            for(SkillManageRecord recode : params){
                if("mcart".equals(type)){ //대표카트
                    updateSql ="UPDATE wdm.USER_INFO SET rprs_skill_cart = :cart  , curr_skill_cart = :cart ,MODIFY_DATE = :updDt WHERE gcloud_uuid = :userId";
                }else if ("scart".equals(type)){// 현재카트
                    updateSql = "UPDATE wdm.USER_INFO SET curr_skill_cart = :cart  , MODIFY_DATE = :updDt WHERE gcloud_uuid = :userId";
                }else if ("mcartback".equals(type)){ //대표카트 원복
                    updateSql ="UPDATE wdm.USER_INFO SET curr_skill_cart = rprs_skill_cart  , MODIFY_DATE = :updDt WHERE gcloud_uuid = :userId";
                }
                FineLoggerFactory.getLogger().error(updateSql);
                jdbcUtils.batchUpdate(updateSql,recode.getDataList());
            }

            //jdbcUtils.batchUpdate(updateSql, (List<Map<String, ?>>) paramMap.get("dataList"));
            //String insertSql = "INSERT INTO finer.realtime_col_personalize (user_id, page, type, col, sort, value) VALUES (:user_id, :page, :type, :col, :sort, :value)";
            //jdbcUtils.batchUpdate(insertSql, (List<Map<String, ?>>) paramMap.get("dataList"));

        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("resResult","API CALL ERROR SKILLUPDATE");
            resMap.put("resCode","999");
            return resMapToJson(resMap);
        }

        return resMapToJson(resMap);
    }


    @RequestMapping(value="/uplus/skill/register", method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String skillRegister_skill(@RequestBody String params) throws Exception {
        String responseString ="";
        Map<String,Object> resMap = new HashMap<>();
        FineLoggerFactory.getLogger().error("=========================== SKILLREGISTER_SKILL START ==============");
        FineLoggerFactory.getLogger().error("SKILLREGISTER PARAMS========" + params);
        try{
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost postRequest = new HttpPost(apiGwSkillRegister);
            postRequest.setHeader("Accept", "application/json");
            postRequest.setHeader("Connection", "keep-alive");
            postRequest.setHeader("Content-Type", "application/json");

            postRequest.setEntity(new StringEntity(params));
            HttpResponse response = client.execute(postRequest);

            ResponseHandler<String> handler = new BasicResponseHandler();
            responseString = handler.handleResponse(response);

            //Response 출력
            if (response.getStatusLine().getStatusCode() == 200) {
                FineLoggerFactory.getLogger().error("up_response 200 ====" + responseString);
            } else {
                FineLoggerFactory.getLogger().error("up_response is error : " + response.getStatusLine().getStatusCode());
                FineLoggerFactory.getLogger().error("up_response toString === " + responseString);
            }

            resMap.put("resResult",responseString);
            resMap.put("resCode",response.getStatusLine().getStatusCode());
            resMapLog(resMap);

        } catch (Exception e){
            FineLoggerFactory.getLogger().error(e.toString());
            resMap.put("resResult","API CALL ERROR SKILLREGISTER");
            resMap.put("resCode","999");
        }
        return resMapToJson(resMap);
    }

    @RequestMapping(value="/uplus/skill/register" , method = RequestMethod.PUT,produces = "application/json;charset=utf-8")
    @ResponseBody
    public Map<String,Object> skillRegister_skill_cart(@RequestBody String params) throws IOException {
        FineLoggerFactory.getLogger().error("=========================== SKILLREGISTER_CART START ==============");
        FineLoggerFactory.getLogger().error("SKILLREGISTER_CART PARAMS ========" + params);
        Map<String,Object> resMap = new HashMap<>();
        String responseString ="";
        try{
            HttpClient client = HttpClientBuilder.create().build();
            HttpPut putRequest = new HttpPut(apiGwSkillRegister);
            putRequest.setHeader("Accept", "application/json");
            putRequest.setHeader("Connection", "keep-alive");
            putRequest.setHeader("Content-Type", "application/json");
            putRequest.setEntity(new StringEntity(params));
            //API CALL
            HttpResponse response = client.execute(putRequest);
            //응답값
            ResponseHandler<String> handler = new BasicResponseHandler();
            responseString = handler.handleResponse(response);

            //Response 출력
            if (response.getStatusLine().getStatusCode() == 200) {
                FineLoggerFactory.getLogger().error("up_response 200 ====" + responseString);
            } else {
                FineLoggerFactory.getLogger().error("up_response is error : " + response.getStatusLine().getStatusCode());
                FineLoggerFactory.getLogger().error("up_response toString === " + responseString);
            }
            resMap.put("resResult",responseString);
            resMap.put("resCode",response.getStatusLine().getStatusCode());
            resMapLog(resMap);
        } catch (Exception e){
            FineLoggerFactory.getLogger().error(e.toString());
            resMap.put("resResult","API CALL ERROR SKILLREGISTER_CART");
            resMap.put("resCode","999");
        }
        return resMap;
    }

    @RequestMapping(value="/uplus/skill/register" , method = RequestMethod.DELETE,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String skillRegister_skill_del(@RequestBody String params ) throws Exception {
        String responseString ="";
        Map<String,Object> resMap = new HashMap<>();
        FineLoggerFactory.getLogger().error("=========================== SKILLREGISTER_SKILL_DEL START ==============");
        FineLoggerFactory.getLogger().error("SKILLREGISTER_SKILL_DEL PARAMS ========" + params);
        try{
            HttpClient httpclient = HttpClientBuilder.create().build();

            String url = apiGwSkillRegister;

            HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
            StringEntity input = new StringEntity(params, ContentType.APPLICATION_JSON);
            httpDelete.setEntity(input);

            HttpResponse response = httpclient.execute(httpDelete);

            ResponseHandler<String> handler = new BasicResponseHandler();
            responseString = handler.handleResponse(response);

            resMap.put("resResult",responseString);
            resMap.put("resCode",response.getStatusLine().getStatusCode());
            // response Log
            resMapLog(resMap);
        } catch (Exception e){
            FineLoggerFactory.getLogger().error(e.toString());
            resMap.put("resResult","API CALL ERROR SKILLREGISTER_DEL");
            resMap.put("resCode","999");
        }
        return resMapToJson(resMap);
    }

    public void resMapLog (Map<String,Object> resMap) {
        for (String key : resMap.keySet()) {
            FineLoggerFactory.getLogger().error(String.format("key : %s, value : %s", key, resMap.get(key))
            );
        }
    }
    public String resMapToJson(Map<String,Object> resMap) throws Exception {
        FineLoggerFactory.getLogger().error("======================resMapTojson ========================");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValueAsString(resMap);
        return mapper.writeValueAsString(resMap);
    }



    /*
     * 아래는 테스트 케이스
     * ip:port/webroot/decision/uplus/skill/register/test
     * mothod : POST,PUT,DELETE
     * */


    @RequestMapping(value="/uplus/skill/register/test" , method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String skillRegister_skill_test(@RequestBody String params) throws IOException {
        String responseString ="";
        FineLoggerFactory.getLogger().error("skilLRegister_skill_test_param =" + params);
        return params;
    }


    @RequestMapping(value="/uplus/skill/register/test" , method = RequestMethod.PUT,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String skillRegister_skill_cart_test(@RequestBody String params) throws IOException {
        String responseString ="";
        FineLoggerFactory.getLogger().error("skilLRegister_skill_cart_test_param =" + params);
        return params;
    }



    @RequestMapping(value="/uplus/skill/register/test" , method = RequestMethod.DELETE,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String skillRegister_skill_del_test(@RequestBody String params) throws IOException {
        String responseString ="";
        FineLoggerFactory.getLogger().error("skilLRegister_skill_del_test_param =" + params);
        return params;
    }
}




class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "DELETE";

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
    public HttpDeleteWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public HttpDeleteWithBody(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpDeleteWithBody() {
        super();
    }
}