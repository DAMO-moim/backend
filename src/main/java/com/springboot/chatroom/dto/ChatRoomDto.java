package com.springboot.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ChatRoomDto {
    @Getter
    @AllArgsConstructor
    public static class Response{
        private long chatRoomId;
        private long categoryId;
        private String categoryName;
    }
}
