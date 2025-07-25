package com.common.develop;

import com.fr.log.FineLoggerFactory;
import com.fr.third.fasterxml.jackson.core.JsonProcessingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Map;

public class CommonAesUtil {
    private Common common;

    public CommonAesUtil() {
        this.common = Common.getInstance();
    }

    static {
        String keyValue = "";
        String keyAlg = "";
        String keyDecryptChar = "";
        try {
            keyValue = CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","sso.decrypt.key",CommonAesUtil.class);
            keyAlg = CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","sso.decrypt.alg",CommonAesUtil.class);
            keyDecryptChar = CommonPropertiesUtil.getPropertyValue("conf/CommLogin.properties","sso.decrypt.char",CommonAesUtil.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        _256key = keyValue;
        alg = keyAlg;
        decryptChar = keyDecryptChar;
    }

    public static final String alg ;
    public static final String _256key ;
    public static final String decryptChar;
    public static final String iv = _256key.substring(0, 16); // 16byte


    /***************************
     공통 로그인 AES 암호화
     ***************************/
    public String encrypt(Map<String,Object> parameterMap) throws Exception {

        Cipher cipher = Cipher.getInstance(alg);
        byte[] key = java.util.Base64.getDecoder().decode(_256key);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(common.getMapValue(parameterMap,"userName").getBytes(decryptChar));
        return java.util.Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(Map<String,Object> parameterMap) throws Exception {
        FineLoggerFactory.getLogger().error(parameterMap.toString());
        Cipher cipher = Cipher.getInstance(alg);
        byte[] key = java.util.Base64.getDecoder().decode(_256key);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = java.util.Base64.getDecoder().decode(common.getMapValue(parameterMap,"userName"));
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, decryptChar);

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



}
