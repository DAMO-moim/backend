package com.springboot.config;

import com.springboot.auth.filter.JwtAuthenticationFilter;
import com.springboot.auth.filter.JwtVerificationFilter;
import com.springboot.auth.handler.MemberAccessDeniedHandler;
import com.springboot.auth.handler.MemberAuthenticationEntryPoint;
import com.springboot.auth.handler.MemberAuthenticationFailureHandler;
import com.springboot.auth.handler.MemberAuthenticationSuccessHandler;
import com.springboot.auth.jwt.JwtTokenizer;
import com.springboot.auth.utils.AuthorityUtils;
import com.springboot.auth.utils.MemberDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

//Spring Security를 설정하기 위한 클래스 (접근권한 등)
//이후 수정이 필요할 수 있다.
//CrossOrigin은 해당 주소에서 오는 요청만 허용한다.
@Configuration
@CrossOrigin(origins = "http://localhost:3000")
public class SecurityConfiguration {
    private final JwtTokenizer jwtTokenizer;
    private final AuthorityUtils authorityUtils;
    private final MemberDetailsService memberDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;

    public SecurityConfiguration(JwtTokenizer jwtTokenizer, AuthorityUtils authorityUtils,
                                 MemberDetailsService memberDetailsService, RedisTemplate<String, Object> redisTemplate) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.memberDetailsService = memberDetailsService;
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .cors(Customizer.withDefaults())// CORS 설정 활성화
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        //WebSocket 관련 경로 허용
                        .antMatchers("/ws-stomp/**", "/pub/**", "/sub/**").permitAll()
                        //Admin
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        //Memeber
                        .antMatchers(HttpMethod.POST, "/members").permitAll()
                        .antMatchers(HttpMethod.GET, "/members").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET,"/members/**").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.PATCH, "/members/**").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE, "/members").hasAnyRole("USER")
                        .antMatchers(HttpMethod.DELETE, "/members/**").hasAnyRole("USER", "ADMIN")
                        //Group
                        .antMatchers(HttpMethod.POST, "/groups").hasRole("USER")
                        .antMatchers(HttpMethod.POST, "/groups/**").hasRole("USER")
                        .antMatchers(HttpMethod.GET, "/groups").hasRole("USER")
                        .antMatchers(HttpMethod.GET,"/groups/**").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.PATCH, "/groups/**").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE, "/groups").hasAnyRole("USER")
                        .antMatchers(HttpMethod.DELETE, "/groups/**").hasAnyRole("USER", "ADMIN")
                        //Board
                        .antMatchers(HttpMethod.POST, "/boards").hasRole("USER")
                        .antMatchers(HttpMethod.GET, "/boards").hasRole("USER")
                        .antMatchers(HttpMethod.GET,"/boards/**").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.PATCH, "/boards/**").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE, "/boards/**").hasAnyRole("USER", "ADMIN")
                        //Comment
                        .antMatchers(HttpMethod.POST, "/comments").hasRole("USER")
                        .antMatchers(HttpMethod.GET, "/comments").hasRole("USER")
                        .antMatchers(HttpMethod.GET,"/comments/**").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.PATCH, "/comments/**").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE, "/comments/**").hasAnyRole("USER", "ADMIN")
                        //My
                        .antMatchers("/chatrooms").hasRole("USER")
                        .antMatchers("/chatrooms/**").hasRole("USER")
                        //categoriy
                        .antMatchers("/catogories").hasRole("USER")
                        //chatroom
                        .antMatchers("/mypage").hasRole("USER")
                        .antMatchers("/mypage/**").hasRole("USER")
                        //Schedules
                        .antMatchers(HttpMethod.POST, "/schedules").hasRole("USER")
                        .antMatchers(HttpMethod.POST, "/schedules/**").hasRole("USER")
                        .antMatchers(HttpMethod.POST, "/schedules/**").hasRole("USER")
                        .antMatchers(HttpMethod.GET, "/schedules").hasRole("USER")
                        .antMatchers(HttpMethod.GET, "/schedules/**").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE, "/schedules/**").hasRole("USER")
                        //모임일정 참여내역부분
                        .antMatchers(HttpMethod.POST, "/schedules/**/participation").hasRole("USER")
                        .antMatchers(HttpMethod.GET, "/schedules/**/participation").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE, "/schedules/**/participation").hasRole("USER")
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // CorsConfigurationSource : CORS 정책 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 모든 출처에서 통신 허용
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://3.39.190.50"));
//        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000"));
        // 전체 요청 -> 특정 요청 Arrays.asList("http://localhost:3000");
        // 지정한 HTTP Method 에 대한 통신 허용
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PATCH", "DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //클라이언트에서 해당 헤더를 사용할 수 있도록 설정한다. ( 설정하지 않으면 아래 헤더만 받음 )
        //Cache-Control, Content-Language ,Content-Type, Expires, Last-Modified, Pragma
        configuration.setAllowedHeaders(Arrays.asList("RefreshToken", "Authorization", "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "RefreshToken"));
        configuration.setAllowCredentials(true);
        //모든 URL에 지금까지 구성한 CORS 정책을 적용.
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    //커스텀 필터 등록 메서드
    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            //필터를 등록하기 위해 매니저를 등록
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            //필터 객체 생성하며 필요한 객체를 DI 시켜준다.
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);
            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");

            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());

            // Redis에서 검증하기 위해 RedisTemplate를 생성자로 전달해준다.
            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils
                    ,memberDetailsService, redisTemplate);
            //addfliter()는 필터 내부에서 체인필터에 등록시킨다.
            builder
                    .addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
        }
    }
}
