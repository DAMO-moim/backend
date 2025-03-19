package com.springboot.auth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorityUtils {
    @Value("${mail.address.admin}")
    private String adminMailAddress;

    //관리자의 권한목록 객체 생성
    private final List<GrantedAuthority> ADMIN_ROLE = org.springframework.security.core.authority.AuthorityUtils
            .createAuthorityList("ROLE_ADMIN","ROLE_USER");
    //일반 사용자의 권한목록 객체 생성
    private final List<GrantedAuthority> USER_ROLE = org.springframework.security.core.authority.AuthorityUtils
            .createAuthorityList("ROLE_USER");

    //데이터베이스에 권한 목록 정보를 저장하기 위한 필드 선언
    private final List<String>ADMIN_ROLES_STRING = List.of("ADMIN","USER");
    private final List<String> USER_ROLES_STRING = List.of("USER");

    //사용자의 권한을 데이터베이스에 저장하기 위한 메서드
    public List<String> createAuthorities(String email){
        if(email.equals(adminMailAddress)){
            return ADMIN_ROLES_STRING;
        }else {
            return USER_ROLES_STRING;
        }
    }

    //데이터베이스에 저장되어있는 권한정보 목록(Role)을 가져와 Context에 주기위한 메서드
    public List<GrantedAuthority> createAuthorities(List<String> roles){
        List<GrantedAuthority> authorities = roles.stream()
                //SimpleGrantedAuthority 클래스는 권한을 생성해주는 클래스
                //반드시 객체 생성시 파라미터로 넘기는값은 ROLE_이 붙어야한다
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        return authorities;
    }
}
