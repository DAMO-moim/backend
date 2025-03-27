package com.springboot.auth.filter;

import com.springboot.auth.jwt.JwtTokenizer;
import com.springboot.auth.utils.AuthorityUtils;
import com.springboot.auth.utils.MemberDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final AuthorityUtils authorityUtils;
    private final MemberDetailsService memberDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;

    public JwtVerificationFilter(JwtTokenizer jwtTokenizer, AuthorityUtils authorityUtils,
            MemberDetailsService memberDetailsService, RedisTemplate<String, Object> redisTemplate) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.memberDetailsService = memberDetailsService;
        this.redisTemplate = redisTemplate;
    }

    //실제 검증을 진행하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        // ✅ WebSocket 요청은 필터 건너뜀
        // 웹소켓을 연결하면 토큰을 검증하는데 여기서 검증하는 jwt 검증 로직은 HTTP 연결방식 (500에러뜸)
        // 그렇기에 WebSocket 연결은 별도 처리 (보통은 StompHandler에서 토큰 검증)
        String uri = request.getRequestURI();
        if (uri.startsWith("/ws-stomp")) {
            filterChain.doFilter(request, response);
            return;
        }

        //서명 검증에서 발생할 수 있는 Exception 예외 처리
        //예외가 발생하면 SecurityContext에 클라이언트 인증정보(Authentication 객체)가 저장되지 않는다.
        //저장되지 않으면, Security Filter 내부에서 AuthenticationException이 발생 -> AuthenticationEntryPoint가 처리
        //발생한 예외를 HttpServletRequset의 애트리뷰트로 추가한다.
        try{
            Map<String, Object> claims = verifyJws(request);
            // Redis에서 토큰 검증하기 위한 메서드
            isTokenValidInRedis(claims);
            setAuthenticationToContext(claims);
        }catch (SignatureException se){
            request.setAttribute("exception",se);
        }catch (ExpiredJwtException ee){
            request.setAttribute("exception",ee);
        }catch (Exception e){
            request.setAttribute("exception",e);
        }

        filterChain.doFilter(request, response);
    }

    private Map<String, Object> verifyJws(HttpServletRequest request){
        //로그인 인증이 성공되면 서버 측에서 Authorization Header에 JWT를 추가했다.
        //클라이언트가 response header로 전달받은 JWT를 request header에 추가해서 서버 측에 전송
        //request의 header에서 JWT를 얻어오며, replace()메서드를 통해 Bearer부분을 제거
        String jws = request.getHeader("Authorization").replace("Bearer ", "");

        //JWT 서명을 검증하기 위한 Secret Ket를 얻어온다.
        String base64EncodedSecretKey = jwtTokenizer.encodedBase64SecretKey(jwtTokenizer.getSecretKey());
        //JWT에서 Claims를 파싱한다. Claims가 정상적으로 파싱되면 서명 검증이 성공된 것
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();
        return claims;
    }

    //Authentication 객체를 SecurityContext에 저장하기 위한 메서드
    private void setAuthenticationToContext(Map<String, Object> claims) {
        //JWT에서 파싱한 Claims에서 username을 얻어온다.
        String username = (String) claims.get("username");
        //SecurityContextHolder에 이메일을 넣어 UserDetails를 넣어준다. (검증된 사용자를 찾기위해)
        //JWT에서 파싱한 username을 알아와서 그 사용자의 정보를 가져와야한다.
        UserDetails memberDetails = memberDetailsService.loadUserByUsername(username);

        //JWT의 Claims에서 얻은 권한 정보를 기반으로 List<GrantedAuthority>생성
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List) claims.get("roles"));

        //username과 List<GrantedAuthority>를 포함한 Authentication 객체를 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, null, authorities);

        //SecurityContext에 Authentication 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    //특정 조건에 부합(true)하면 해당 filter의 동작이 수행하지 않고 다음 filter로 건너뛰게 해준다.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //request header에서 Authorization의 값을 얻어온다.
        String authorization = request.getHeader("Authorization");

        //만약 null 또는 Bearer로 시작하지 않는다면 해당 Filter의 동작은 수행하지 않도록 한다.
        //즉, JWT가 Authorization header에 포함되지 않았다면
        //JWT 자격증명이 필요없는 리소스에 대한 요청이라고 판단하여 건너뛰는 것
        return authorization == null || !authorization.startsWith("Bearer");
    }

    // Redis에서 토큰을 검증하는 메서드 추가
    private void isTokenValidInRedis(Map<String, Object> claims) {
        String username = Optional.ofNullable((String) claims.get("username"))
                .orElseThrow(() -> new NullPointerException("Username is null"));

        // Redis에 해당 키(username)가 존재하는지 확인
        Boolean hasKey = redisTemplate.hasKey(username);

        // 키가 존재하지 않거나 null일 경우 예외를 던진다.
        if (Boolean.FALSE.equals(hasKey)) {
            throw new IllegalStateException("Redis key does not exist for username: " + username);
        }
    }
}
