package com.springboot.chatroom.repository;

import com.springboot.chatroom.entity.ChatRoom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    //특정 채팅방 조회 시, 메세지도 함께 로딩되게 설정
    //이 설정을 적용하면 LAZY(지연로딩)이 아닌 EAGER(즉시로딩)된다.
    //채팅방의 ID를 입력받으면 해방 객체가 리턴
    @EntityGraph(attributePaths = {"messages"}) // messages 컬렉션을 즉시 로딩
    Optional<ChatRoom> findById(Long id);
}
