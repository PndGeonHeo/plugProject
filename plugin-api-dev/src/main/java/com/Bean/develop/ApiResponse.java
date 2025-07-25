package com.Bean.develop;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiResponse<T>{
    private boolean success;
    private String message;
    private T data;

    // 기본 생성자
    public ApiResponse() {}

    // 성공한 경우의 응답
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // 데이터를 포함한 응답
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 성공 여부
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // 메시지
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // 데이터 (제네릭 타입)
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // 조건에 맞으면 리다이렉트 처리
    public void handleRedirect(HttpServletResponse response, String redirectUrl) throws IOException {
        if (success) { // 성공할 경우에 리다이렉트 처리
            response.sendRedirect(redirectUrl);
        }
    }


}
