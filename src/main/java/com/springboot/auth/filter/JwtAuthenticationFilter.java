package com.springboot.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.springboot.auth.dto.LoginDto;
import com.springboot.auth.jwt.JwtTokenizer;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.springboot.member.entity.Member;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//클라이언트의 인증 정보를 직접적으로 수신하여 인증 처리하는 필터 클래스
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    //로그인 인증정보를 전달받아 UserDetailsService와 인터렉션 -> 인증여부를 파악하기 위해 DI
    private final AuthenticationManager authenticationManager;
    //인증 성공 -> JWT 토큰 생성,발행 위해 DI
    private final JwtTokenizer jwtTokenizer;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
    }

    //인증 메서드 재정의
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //클라이언트로부터 받은 로그인 정보를 DTO 클래스로 역직렬화 하기 위해  ObjectMapper 인스턴스 생성
        //스프링이 아닌 서블릿 영역이기 때문에 직접 매핑해야한다.
        ObjectMapper objectMapper = new ObjectMapper();

        //ServletInputStream을 loginDto 클래스의 객체로 역직렬화
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

        //Username, Password 정보를 포함한 UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        //위에서 생성한 토큰을 AuthenticationManager에게 전달하여 인증처리 위임
        //AuthenticationManager가 실행하면서 인증을 처리한다.
        return authenticationManager.authenticate(authenticationToken);
    }

    //인증 성공시 호출되는 메서드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //AuthenticationManager 내부에서 인증 성공시 인증된 Authentication 객체가 생성된다.
        //인증객체가 생성되면 principal필드에 Member 엔티티 클래스의 객체가 할당
        Member member = (Member) authResult.getPrincipal();

        //액세스 토큰, 리플래시 토큰 생성
        String accessToken = delegateAccessToken(member);
        String refreshToken = delegateRefreshToken(member, accessToken);

        //response header에 accessToken를 추가한다.
        //AccessToken은 클라이언트 측에서 백엔드 측에 요청을 보낼때마다 request header에 추가해서 클라이언트 측의 자격을 증명하는 데 사용된다.
        response.setHeader("Authorization", "Bearer " + accessToken);
        //RefreshToken를 추가한다. 액세스토큰을 재발급하기 위해 사용
        response.setHeader("Refresh", refreshToken);
        response.setHeader("MemberId", String.valueOf(member.getMemberId()));

        String message = "로그인에 성공하셨습니다.";
        Gson gson = new Gson();
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(gson.toJson(Map.of("message", message)));
    }

    //액세스 토큰 생성 메서드 구현
    private String delegateAccessToken(Member member){
        //사용자 정보 저장
        Map<String, Object> claims = new HashMap<>();
        //claims에 memeberId를 넣지 않으면 메세지를 입력할때마다 db에서 회원을 조회해야 하여 성능이 저하됨
        claims.put("memberId", member.getMemberId());
        claims.put("username", member.getEmail());
        claims.put("roles", member.getRoles());

        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodedBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims,subject,expiration,base64EncodedSecretKey);
        return accessToken;
    }

    //리플래시 토큰 생성 메서드
    private String delegateRefreshToken(Member member, String accessToken){
        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodedBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey, accessToken);

        return refreshToken;
    }
}
