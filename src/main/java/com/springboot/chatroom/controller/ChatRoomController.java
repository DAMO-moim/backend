package com.springboot.chatroom.controller;

import com.springboot.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.Mapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatrooms") // ✅ 모든 엔드포인트의 기본 URL
public class ChatRoomController {
    @GetMapping({"category-id"})
    public ResponseEntity getChatRoom(@PathVariable("category-id") long categoryId,
                                      @Schema(hidden = true) @AuthenticationPrincipal Member member){

        return null;
    }

    @GetMapping
    public ResponseEntity getChatRooms(@Schema(hidden = true) @AuthenticationPrincipal Member member){

        return null;
    }
}
