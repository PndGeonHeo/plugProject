package com.controller;

import com.Bean.ApiResponse;
import com.common.*;
import com.fr.data.NetworkHelper;
import com.fr.decision.authority.data.User;
import com.fr.decision.mobile.terminal.TerminalHandler;
import com.fr.decision.service.context.ServiceContext;
import com.fr.decision.webservice.annotation.LoginStatusChecker;
import com.fr.decision.webservice.v10.login.LoginService;
import com.fr.decision.webservice.v10.login.TokenResource;
import com.fr.decision.webservice.v10.user.UserService;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;
import com.fr.stable.web.Device;
import com.fr.third.fasterxml.jackson.core.JsonProcessingException;
import com.fr.third.fasterxml.jackson.databind.ObjectMapper;
import com.fr.third.springframework.beans.factory.annotation.Autowired;
import com.fr.third.springframework.http.MediaType;
import com.fr.third.springframework.http.ResponseEntity;
import com.fr.third.springframework.stereotype.Controller;
import com.fr.third.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value ="/{svc}/{type}")
@LoginStatusChecker(required = false)

public class CommLogin {
    public CommLogin(){
        this.loginConfig = LoginConfig.getInstance();
    }
    @Autowired
    private ServiceContext serviceContext;

    public void setServiceContext(ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }
    private LoginConfig loginConfig;

    public String properties() throws JsonProcessingException {
        Common common = new Common();
        if(common == null) common = new Common();
        return common.getPropertyJsonTree("conf/CommLogin.properties", CommLogin.class);
    }


    private String getAdmin() throws  Exception{
        return CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","login.sec.sub",CommLogin.class);
    }

    //CommLogin.properties AES key get
    @RequestMapping(value="/account/aesKey", method = RequestMethod.GET)
    @ResponseBody
    public String GetAesKey() throws Exception {
        String aesKey = CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","sso.decrypt.key",CommLogin.class);

        return aesKey;
    }

    @RequestMapping(value="/account/rand/Token", method = RequestMethod.GET)
    @ResponseBody
    public String AesRandomKey() throws  Exception{
        byte[] key = new byte[32]; // 256-bit (32 bytes)
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);

        String base64Key = Base64.getEncoder().encodeToString(key);
        System.out.println("Generated AES-256 Key: " + base64Key);
        return base64Key;
    }

    @RequestMapping(value = "/account/signin", method = RequestMethod.GET ,produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String LoginPage(HttpServletRequest req , HttpServletResponse res, @PathVariable("type") String loginType) throws Exception {
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("resultJson",properties());
        //자동로그인
        if(LoginConfig.getInstance().isAuthToken(req,res)){
            this.autoLogin(req,res);
        }
        //로그인 타입 설정
        String pageType = CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","login.type."+loginType,CommLogin.class);
        return WebUtil.parseWebPageResourceSafe(pageType,resultMap,"");
    }

    private void autoLogin (HttpServletRequest request , HttpServletResponse response) throws Exception{
        Device device = NetworkHelper.getDevice(request);
        //자동 로그인을 위한 토큰 확인
        String oldToken = TokenResource.COOKIE.getToken(request);
        if(StringUtils.isNotEmpty(oldToken)){
            LoginService.getInstance().loginStatusValid(oldToken, TerminalHandler.getTerminal(request, device));
            response.sendRedirect(CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","login.redirect.url",CommLogin.class));
        }
    }

    /***************************
     공통 로그인폼 loginType = loin
     (ID,PW사용했을 경우 이용)
     ***************************/
    @RequestMapping(value="/account/signin/login", method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResponseEntity<ApiResponse<String>> loginAction(@RequestBody String params,HttpServletRequest req , HttpServletResponse res,String svc) throws  Exception{
        FineLoggerFactory.getLogger().error(params);

        ApiResponse<String> response = null;
        //usercheck
        if(this.getUserExists(params)){
             response = new ApiResponse<>(false,"USER_NOT_FOUND_MESSAGE",CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","USER_NOT_FOUND_MESSAGE",CommLogin.class));
             return ResponseEntity.status(200).body(response);
        }
        //성공 Response
        response = new ApiResponse<>(true,"LOGIN_SUCCESS",CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","LOGIN_SUCCESS",CommLogin.class));
        try{
            //로그인 처리
            loginConfig.loginAction(params,req , res);
        }catch (Exception e){
            response = new ApiResponse<>(false,"LOGIN_ACTION_ERROR",CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","LOGIN_ACTION_ERROR",CommLogin.class));
            return  ResponseEntity.status(500).body(response);
        }

        return  ResponseEntity.status(200).body(response);
    }

    /**
     * 사용자 Password get
     */
    @RequestMapping(value="/account/signin/login/pw", method = RequestMethod.GET)
    @ResponseBody
    private String getLoginPw(@RequestParam("username") String username) throws Exception{
        User user = UserService.getInstance().getUserByUserName(username); //유저정보 취득
        FineLoggerFactory.getLogger().error("loginPw===" + user.getPassword());
        return (String)user.getPassword();
    }

    //로그인 가능여부 확인
    private boolean getUserExists(String params) throws Exception{
        boolean userExists = true;
        User user = UserService.getInstance().getUserByUserName(CommonJsonUtil.getStringFromJsonObject(CommonJsonUtil.parseJsonObject(params),"username")); //유저정보 취득

        if(user == null){
            return userExists;
        }else {// FineDB User password = Client password 비교
            if(user.getPassword().equals(CommonJsonUtil.getStringFromJsonObject(CommonJsonUtil.parseJsonObject(params),"password"))){
                userExists = false;
            }
        }

        return userExists;
    }

    /**
     * 고객사 SSO 리다이렉트
     */
    @RequestMapping(value="/account/signin/sso", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ApiResponse<String>> getSsoParams(HttpServletRequest req , HttpServletResponse res) throws  Exception {
        String aa = req.getQueryString();
        FineLoggerFactory.getLogger().error(aa);
        WebUtil webUtil = new WebUtil();
        Map<String,Object> queryStringMap = webUtil.parseQueryString(req.getQueryString());
        ApiResponse<String> response = null;

        Class clazz = getDecryptType();
        if(clazz == null) {
            response = new ApiResponse<>(false,"DESCRYPT_CHECK_ERROR",CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","DESCRYPT_CHECK_ERROR",CommLogin.class));
            return ResponseEntity.status(401).body(response);
        }
        /** 복화화 시작하여 userName GET*/
        String decryptedValue="";
        try{
            decryptedValue = invokeDecryptMethod(clazz, "decrypt", queryStringMap);
            if(decryptedValue.isEmpty()){
                response = new ApiResponse<>(false,"DESCRYPT_USER_ERROR",CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","DESCRYPT_USER_ERROR",CommLogin.class));
                return ResponseEntity.status(401).body(response);
            }
        }catch (Exception e) {
            response = new ApiResponse<>(false,"DESCRYPT_PARAM_ERROR",CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","DESCRYPT_PARAM_ERROR",CommLogin.class));
            return ResponseEntity.status(401).body(response);
        }
        /** 로그인 처리 Map 갱신 */
        queryStringMap.put("username",decryptedValue);

        /**로그인 처리 */
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            loginConfig.loginAction(objectMapper.writeValueAsString(queryStringMap),req,res);
        }catch (Exception e){
            response = new ApiResponse<>(false,"LOGIN_ACTION_ERROR",CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","LOGIN_ACTION_ERROR",CommLogin.class));
        }

        /** 로그인 성공 시, 페이지 이동*/
        response = new ApiResponse<>(true,"LOGIN_SUCCESS",CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","LOGIN_SUCCESS",CommLogin.class));
        response.handleRedirect(res,CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties", "login.redirect.url", CommLogin.class));
        return ResponseEntity.status(200).body(response);
    }
    /**
     * 복호화 방식
     */
    private Class<?> getDecryptType() throws IOException ,Exception{
        String decryptType = CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","sso.decrypt.type",CommLogin.class);
        if("AES".equals(decryptType)){
            return CommonAesUtil.class;
        }else if ("ARIA".equals(decryptType)){
//            return ARIAUtil.class;
        }
        return null;
    }

    private String invokeDecryptMethod(Class<?> clazz, String methodName, Map<String, Object> params) throws Exception {
        // 클래스의 메서드 가져오기
        Method method = clazz.getDeclaredMethod(methodName, Map.class);
        // 메서드 접근 가능 설정
        method.setAccessible(true);
        // 클래스의 인스턴스 생성 (static 메서드가 아니라면)
        Object instance = clazz.getDeclaredConstructor().newInstance();
        // 메서드 호출
        Object result = method.invoke(instance, params);
        // 결과 반환
        return result != null ? result.toString() : null;
    }

}

