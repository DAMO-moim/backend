package com.springboot.chatroom.principal;

import java.security.Principal;

//STOMP 프로토콜에서 사용할 사용자 인증 객체
//웹 소켓사용시 특정 사용자의 고유ID(이름)을 저장하는 커스텀 Principal 클래스
//STOMP 연결에서 사용자를 식별하기 위해 사용된다.
//jwt 기반 인증 방식에선 사용자의 username를 저장한다
public class StompPrincipal implements Principal {
    private final String name;

    public StompPrincipal(String name) {
        this.name = name;
    }

    //사용자 username반환
    @Override
    public String getName() {
        return name;
    }
}
