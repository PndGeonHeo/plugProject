package com.common.develop;

import com.controller.develop.CommLogin;
import com.fr.base.TemplateUtils;
import com.fr.data.NetworkHelper;
import com.fr.decision.authority.data.User;
import com.fr.decision.config.FSConfig;
import com.fr.decision.mobile.terminal.TerminalHandler;
import com.fr.decision.webservice.utils.ControllerFactory;
import com.fr.decision.webservice.utils.controller.AuthenticController;
import com.fr.decision.webservice.v10.login.LoginService;
import com.fr.decision.webservice.v10.login.TokenResource;
import com.fr.decision.webservice.v10.user.UserService;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.security.JwtUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.web.Device;
import com.fr.third.fasterxml.jackson.core.JsonProcessingException;
import com.fr.third.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Map;

public class LoginConfig {
    private static LoginConfig loginConfigInstance = null;

    static {
        Common common = new Common();
        if (common == null) common = new Common();
        ObjectMapper objectMapper = new ObjectMapper();
        String keyValue = "";
        try {
            Map<String, Object> parsedJson = objectMapper.readValue(common.getPropertyJson("conf/CommLogin.properties", CommLogin.class), Map.class);
            keyValue = (String) parsedJson.get("login.sec.point");

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        _256key = keyValue;

    }

    public static final String alg = "AES/CBC/PKCS5Padding";
    public static final String _256key;
    public static final String iv = _256key.substring(0, 16); // 16byte

    public LoginConfig() {

    }

    // LoginConfig 객체 반환 (싱글톤 패턴 사용)
    public static LoginConfig getInstance() {
        if (loginConfigInstance == null) {
            synchronized (LoginConfig.class) {
                if (loginConfigInstance == null) {
                    loginConfigInstance = new LoginConfig(); // 객체가 없으면 생성
                }
            }
        }
        return loginConfigInstance;
    }


    /***************************
     2024 대상 로그인 AES 암호화 현재 대상에서 같은위치로 사용하고 있어 변경 불가
     decrypt()
     ***************************/
    public static String decrypt(String encryptedValue, String iv, String salt, String password) throws Exception {
        // Base64 디코딩
        byte[] encryptedBytes = Base64.decodeBase64(encryptedValue);
        byte[] ivBytes = hexStringToByteArray(iv);
        byte[] saltBytes = hexStringToByteArray(salt);
        // AES 키 생성
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), saltBytes, 100, 128);
        SecretKey secretKey = new SecretKeySpec(factory.generateSecret(keySpec).getEncoded(), "AES");

        // 복호화
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(ivBytes));
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // 결과 출력
        String decryptedValue = new String(decryptedBytes, StandardCharsets.UTF_8);
        return decryptedValue;

    }

    // 16진수 문자열을 바이트 배열로 변환
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }


    /***************************
     로그인 처리 메인 메서드
     공통 로그인폼 loginType = loin
     (ID,PW사용했을 경우 이용)
     ***************************/
    public void loginAction(String params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean needLogin = true;
        String loginRedirectUrl = CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties", "login.redirect.url", LoginConfig.class);
        String templateRenderUrl = CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties", "template.render", LoginConfig.class);

        String username = CommonJsonUtil.getStringFromJsonObject(CommonJsonUtil.parseJsonObject(params), "username");

        // 템플릿 렌더링
        renderTemplate(templateRenderUrl);

        try {
            User user = UserService.getInstance().getUserByUserName(username);
            Device device = NetworkHelper.getDevice(request);
            String oldToken = TokenResource.COOKIE.getToken(request);

            // 로그인 필요 여부 확인
            needLogin = isLoginRequired(oldToken, username, request, device);

            if (needLogin) {
                processLogin(user, request, response, device);
            }

            if (!needLogin) {
                response.sendRedirect(loginRedirectUrl);
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error("Error occurred during login", e);
            e.printStackTrace();
        }
    }

    /**
     * 템플릿 렌더링
     */
    private void renderTemplate(String templateRenderUrl) {
        try {
            TemplateUtils.render(templateRenderUrl);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error("Template rendering failed: " + e.getMessage(), e);
        }
    }

    /**
     * 로그인 필요 여부 검증
     */
    private boolean isLoginRequired(String oldToken, String username, HttpServletRequest request, Device device) {
        if (StringUtils.isEmpty(oldToken)) {
            return true;
        }

        try {
            String tokenUsername = JwtUtils.parseJWT(oldToken).getSubject();
            if (!ComparatorUtils.equals(username, tokenUsername)) {
                FineLoggerFactory.getLogger().info("Username changed: " + username);
            }
            LoginService.getInstance().loginStatusValid(oldToken, TerminalHandler.getTerminal(request, device));
            return false;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().info("Token validation failed: " + e.getMessage(), e);
            return true;
        }
    }


    /**
     * 로그인 필요 여부 검증
     */
    public boolean isAuthToken(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String oldToken = TokenResource.COOKIE.getToken(request);
        Device device = NetworkHelper.getDevice(request);
        if (StringUtils.isEmpty(oldToken)) {
            try {
                LoginService.getInstance().logout(request,response);
            }catch (Exception ee){
                FineLoggerFactory.getLogger().error("oldToken Expire");
            }finally {
                return false;
            }
        } else {
            try {
                LoginService.getInstance().loginStatusValid(oldToken, TerminalHandler.getTerminal(request, device));
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }




    /**
     * 로그인 처리
     */
    private void processLogin(User user, HttpServletRequest request, HttpServletResponse response, Device device) throws Exception {
        TerminalHandler terminal = TerminalHandler.getTerminal(request, device);
        AuthenticController authenticController = ControllerFactory.getInstance().getAuthenticController(user.getId());
        long tokenTimeout = FSConfig.getInstance().getLoginConfig().getLoginTimeout();

        // 토큰 생성
        String token = generateToken(user, tokenTimeout);

        // 중복 로그인 처리
        authenticController.verifySingleLoginStatus(user.getUserName(), terminal, token);
        authenticController.logoutSingleLoginInvalidUser(user.getUserName(), terminal);

        // 실제 로그인 수행
        LoginService.getInstance().login(request, response, user.getUserName());
    }

    /**
     * 토큰 생성
     */
    private String generateToken(User user, long tokenTimeout) throws Exception {
        Method method = LoginService.class.getDeclaredMethod("generateToken", String.class, String.class, String.class, long.class);
        method.setAccessible(true);
        return (String) method.invoke(LoginService.class.newInstance(), user.getUserName(), user.getDisplayName(), user.getTenantId(), tokenTimeout);
    }


}

