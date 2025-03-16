package com.springboot.auth.controller;

import com.springboot.auth.service.AuthService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 로그아웃을 하기 위한 컨트롤러 계층 구현
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/logout") // "/auth/logout" 경로로 POST 요청을 처리하는 메서드로 지정합니다.
    public ResponseEntity postLogout(Authentication authentication) {
        //만약 사용자가 로그인을 하지않았을 경우 NPE 처리를 위한 예외처리
        if (authentication == null){
            throw new BusinessLogicException(ExceptionCode.USER_NOT_LOGGED_IN);
        }

        String username = authentication.getName(); // 현재 인증된 사용자의 사용자명을 가져옵니다.

        // AuthService의 logout 메서드를 호출하여 로그아웃을 처리하고, 결과에 따라 HTTP 상태 코드를 반환합니다.
        authService.logout(username);

        //로그아웃이 실패하면 예외를 던진다.
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
