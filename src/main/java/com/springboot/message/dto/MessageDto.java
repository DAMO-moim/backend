package com.springboot.message.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
        private long messageId;               // 메시지의 고유 ID (DB에서 생성됨)
        private String content;        // 메시지 내용
        private String writer;         // 메시지 작성자
        private String writerProfileImage;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt; // 메시지 작성 시간
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatSocketMessage {
        private String type; // type 3가지 : CHAT_MESSAGE, SYSTEM_MESSAGE, MEMBER_COUNT
        private Long chatRoomId;
        private Object payload;
    }
}
