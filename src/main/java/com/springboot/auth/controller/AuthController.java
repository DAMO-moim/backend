package com.springboot.auth.controller;

import com.springboot.auth.dto.LoginDto;
import com.springboot.auth.jwt.JwtTokenizer;
import com.springboot.auth.service.AuthService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import com.springboot.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "공용 컨트롤러", description = "로그인, 로그아웃, 토큰 재발급 컨트롤러")
// 로그아웃을 하기 위한 컨트롤러 계층 구현
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenizer jwtTokenizer;
    private final MemberRepository memberRepository;

    public AuthController(AuthService authService, JwtTokenizer jwtTokenizer, MemberRepository memberRepository) {
        this.authService = authService;
        this.jwtTokenizer = jwtTokenizer;
        this.memberRepository = memberRepository;
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/login")
    public void login(@RequestBody LoginDto loginDto) {
        // Swagger 문서용 가짜 핸들러
    }

    @Operation(summary = "로그아웃", description = "사용자가 로그아웃 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/logout")
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

    @Operation(summary = "액세스 토큰 재발급(자동로그인)", description = "만료된 액세스 토큰을 재발급 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "액세스 재발급 완료"),
            @ApiResponse(responseCode = "401", description = "만료된 리플래시 토큰입니다.")
    })
    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Refresh") String refreshHeader) {
        // 1. "Bearer " 제거
        String refreshToken = refreshHeader.replace("Bearer ", "");

        // 2. Refresh Token 유효성 검증
        if (!jwtTokenizer.validateToken(refreshToken)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        // 3. 사용자 식별 정보 추출
        String email = jwtTokenizer.getClaims(refreshToken,
                        jwtTokenizer.encodedBase64SecretKey(jwtTokenizer.getSecretKey()))
                .getBody()
                .getSubject();

        // 4. 사용자 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        // 5. 새 Access Token 생성
        Map<String, Object> claims = Map.of(
                "memberId", member.getMemberId(),
                "username", member.getEmail(),
                "roles", member.getRoles()
        );

        String newAccessToken = jwtTokenizer.generateAccessToken(
                claims,
                member.getEmail(),
                jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes()),
                jwtTokenizer.encodedBase64SecretKey(jwtTokenizer.getSecretKey())
        );

        // 6. 응답 헤더로 새 토큰 전달
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + newAccessToken)
                .build();
    }
}
