package com.springboot.message.repository;

import com.springboot.message.entity.Message;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    //채팅방 입장시 지금까지 작성된 메세지 중 24시간 이내 메세지만 보이도록 추가
//    @Query("SELECT m FROM Message m WHERE m.chatRoom.chatRoomId = :chatRoomId ORDER BY m.createdAt ASC")
//    List<Message> findRecentMessages(@Param("chatRoomId") Long chatRoomId);

    List<Message> findByChatRoom_ChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);
}
