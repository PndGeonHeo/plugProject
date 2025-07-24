package com.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CommSha256Util {
    /***************************
     공통 sha256 암호화
     ***************************/
    // 문자열을 SHA-256 해시로 변환
    public static String encrypt(String input) throws NoSuchAlgorithmException, NoSuchAlgorithmException {
        // MessageDigest 객체 생성
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // 입력 데이터를 바이트 배열로 변환하여 해시 생성
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        // 바이트 배열을 16진수 문자열로 변환
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b); // 바이트를 16진수로 변환
            if (hex.length() == 1) {
                hexString.append('0'); // 1자리일 경우 앞에 '0' 추가
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}