package com.springboot.message.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class MessageDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        private String content;
        private String writer;    // 메시지 작성자 (WebSocket 연결된 유저명)
        private long memberId;    // 메시지 작성자의 고유 ID (DB 저장을 위해 필요)
        private long chatRoomId;  // 메시지가 속한 채팅방 ID
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;               // 메시지의 고유 ID (DB에서 생성됨)
        private String content;        // 메시지 내용
        private String writer;         // 메시지 작성자
        private LocalDateTime createdAt; // 메시지 작성 시간
    }
}
