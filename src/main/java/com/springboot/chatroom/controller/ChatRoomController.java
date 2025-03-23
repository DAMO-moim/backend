package com.springboot.chatroom.controller;

import com.springboot.chatroom.dto.ChatRoomDto;
import com.springboot.chatroom.entity.ChatRoom;
import com.springboot.chatroom.mapper.ChatMapper;
import com.springboot.chatroom.service.ChatRoomService;
import com.springboot.dto.SingleResponseDto;
import com.springboot.member.entity.Member;
import com.springboot.message.dto.MessageDto;
import com.springboot.message.entity.Message;
import com.springboot.message.mapper.MessageMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chatrooms") // ✅ 모든 엔드포인트의 기본 URL
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatMapper mapper;
    private final MessageMapper messageMapper;

    public ChatRoomController(ChatRoomService chatRoomService, ChatMapper mapper, MessageMapper messageMapper) {
        this.chatRoomService = chatRoomService;
        this.mapper = mapper;
        this.messageMapper = messageMapper;
    }

    //하나의 카테고리 채팅방 조회
    @GetMapping({"/{chatroom-id}"})
    public ResponseEntity getChatRoom(@PathVariable("chatroom-id") long chatRoomId,
                                      @Schema(hidden = true) @AuthenticationPrincipal Member member){
        ChatRoom chatRoom = chatRoomService.findChatRoom(chatRoomId, member.getMemberId());

        // 메시지 조회 (24시간 기준)
        List<Message> messages = chatRoomService.findRecentMessagesForRoom(chatRoomId);

        // DTO 매핑
        List<MessageDto.Response> messageDtos = messageMapper.messagesToMessageResponses(messages);

        ChatRoomDto.MessageResponse response = new ChatRoomDto.MessageResponse(
                chatRoom.getChatRoomId(),
                chatRoom.getCategory().getCategoryId(),
                chatRoom.getCategory().getCategoryName(),
                messageDtos
        );
        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    //회원의 카테고리별 채팅방 목록 조회
    @GetMapping
    public ResponseEntity getChatRooms(@Schema(hidden = true) @AuthenticationPrincipal Member member){

        List<ChatRoom> categoryChat = chatRoomService.findChatRooms(member.getMemberId());
        //List<ChatRoomDto.Response> responses = mapper.chatRoomToChatRoomResponseDtos(cateogryChat);

        //null 필터링
        List<ChatRoomDto.Response> responses = categoryChat.stream()
                .map(mapper::chatRoomToChatRoomResponseDto)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new ResponseEntity(new SingleResponseDto<>(responses), HttpStatus.OK);
    }
}
