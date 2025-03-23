package com.springboot.message.controller;

import com.springboot.chatroom.config.StompHandler;
import com.springboot.chatroom.service.ChatRoomService;

import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import com.springboot.message.dto.MessageDto;
import com.springboot.message.entity.Message;
import com.springboot.message.mapper.MessageMapper;
import com.springboot.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageMapper mapper;
    private final MessageService messageService;
    private final MemberService memberService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    //클라이언트로 메세지 전송 요청을 받으면 클라이언트가 chatRoomID를 가진 채팅방에 메세지를 보낸다.
    @MessageMapping("/chat/{chatRoomId}/sendMessage") // 메시지 발행 엔드포인트
    public void sendMessage(@DestinationVariable String chatRoomId,
                            MessageDto.Post messageDto,
                            SimpMessageHeaderAccessor headerAccessor){
        //Stomp 세션 ID 가져오기
        String sessionId = headerAccessor.getSessionId();

        //세션 id를 기반으로 유저 정보 가져오기
        String username = StompHandler.getUsernameBySessionId(sessionId);
        Long memberId = StompHandler.getMemberIdBySessionId(sessionId);

        Member member = memberService.findVerifiedMember(memberId);
        String writerName = member.getName();
        //테스트하기위해 ID삽입
        messageDto.setChatRoomId(Long.parseLong(chatRoomId));
        //작성자 정보 저장
        messageDto.setWriter(writerName);
        messageDto.setMemberId(memberId);

        // 메시지를 생성하면서 chatRoom을 설정 (DB 저장 전)
        Message message = mapper.messagePostToMessage(messageDto);

        // 메시지를 DB에 저장
        Message savedMessage = messageService.createMessage(message);

        // 저장된 메시지를 구독 중인 클라이언트들에게 전송
        MessageDto.Response responseMessage = mapper.messageToMessageResponse(savedMessage);
        responseMessage.setWriter(username); // 메시지 작성자 정보 추가

        // 해당 채팅방을 구독 중인 모든 클라이언트에게 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, responseMessage);
    }
}
