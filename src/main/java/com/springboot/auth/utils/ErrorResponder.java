package com.springboot.auth.utils;

import com.google.gson.Gson;
import com.springboot.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorResponder {
    public static void sendErrorResponse(HttpServletResponse response,
                                         HttpStatus status)throws IOException {
        Gson gson = new Gson();
        ErrorResponse errorResponse = ErrorResponse.of(status);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        //ErrorReponse 객체를 JSON 포맷 문자열로 변환 후 출력 스트림 생성
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));
    }
}
