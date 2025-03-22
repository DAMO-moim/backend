package com.springboot.chatroom.dto;

import com.springboot.message.dto.MessageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class ChatRoomDto {
    @Getter
    @AllArgsConstructor
    public static class Response{
        private long chatRoomId;
        private long categoryId;
        private String categoryName;
    }

    @Getter
    @AllArgsConstructor
    public static class MessageResponse {
        private long chatRoomId;
        private long categoryId;
        private String categoryName;
        private List<MessageDto.Response> messages;
    }
}
