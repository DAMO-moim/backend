package com.springboot.chatroom.dto;

import com.springboot.message.dto.MessageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class ChatRoomDto {
    @Getter
    @AllArgsConstructor
    public static class Response{
        @Schema(description = "채팅방 ID", example = "1")
        private long chatRoomId;
        @Schema(description = "카테고리 ID", example = "1")
        private long categoryId;
        @Schema(description = "카테고리 명", example = "스포츠")
        private String categoryName;
    }

    @Getter
    @AllArgsConstructor
    public static class MessageResponse {
        @Schema(description = "채팅방 ID", example = "1")
        private long chatRoomId;
        @Schema(description = "카테고리 ID", example = "1")
        private long categoryId;
        @Schema(description = "카테고리 명", example = "스포츠")
        private String categoryName;
        private List<MessageDto.Response> messages;
    }
}
