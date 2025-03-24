package com.springboot.chatroom.event;

import com.springboot.chatroom.config.StompHandler;
import com.springboot.chatroom.tracker.ChatRoomSessionTracker;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import com.springboot.message.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    //클라이언트에게 메세지를 전송하기 위한 객체
    private final SimpMessagingTemplate messagingTemplate;
    //세선과 채팅방 연결관리 클래스
    private final ChatRoomSessionTracker sessionTracker;
    //유저의 이름을 가져오기 위한 서비스 계층 클래스
    private final MemberService memberService;

    // 사용자가 채팅방에 입장할 때 호출(클라이언트가 구독시)
    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage()); //STOMP 헤더 추출
        String destination = accessor.getDestination();  // "/sub/chat/1" 등의 구독 대상 결로를 가져온다.
        String sessionId = accessor.getSessionId(); // 웹 소켓과 연결된 클라이언트의 세션 ID

        if (destination != null && destination.startsWith("/sub/chat/")) {
            //구독 주소에서 채팅방 ID 추출
            String chatRoomId = destination.split("/")[3];
            //세션에서 사용자 정보 추출(jwt 검증 후 매핑)
            String username = StompHandler.getUsernameBySessionId(sessionId);
            long memberId = StompHandler.getMemberIdBySessionId(sessionId);
            Member member = memberService.findVerifiedMember(memberId);
            String name = member.getName();

            //세션과 채팅방 연결을 추척
            boolean added = sessionTracker.addSession(chatRoomId, sessionId, username);
            if(!added){ //이미 구독한 경우 메세지 전송하지 않는다.
                return;
            }

            // 인원 수 갱신 브로드캐스트
            // 현재 채팅방 인원 수 가져오기
            int count = sessionTracker.getSessionCount(chatRoomId);
//            messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId + "/members", count);

            // 모든 클라이언트에게 인원수를 전송
            MessageDto.ChatSocketMessage memberCountMsg = new MessageDto.ChatSocketMessage(
                    "MEMBER_COUNT",
                    Long.parseLong(chatRoomId),
                    Map.of("count", count)
            );
            messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, memberCountMsg);
            // 입장 메시지 출력
            MessageDto.ChatSocketMessage systemMsg = new MessageDto.ChatSocketMessage(
                    "SYSTEM_MESSAGE",
                    Long.parseLong(chatRoomId),
                    Map.of("message", name + "님이 입장했습니다.")
            );
            messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, systemMsg);
        }
    }

    // 사용자가 채팅방에서 퇴장했을 때(클라이언트가 WebSocket 연결을 끊었을 때 호출)
    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        // 끊어진 세션 id
        String sessionId = event.getSessionId();
        // 해당 세션에 연결된 사용자 정보 추출(구독 시점에 유저를 가져옴)
        String username = sessionTracker.getUsername(sessionId);
        long memberId = StompHandler.getMemberIdBySessionId(sessionId);
        Member member = memberService.findVerifiedMember(memberId);
        String name = member.getName();

        // 모든 채팅방에 대해 세션을 찾아서 제거
        for (Map.Entry<String, Set<String>> entry : sessionTracker.getAllSessions().entrySet()) {
            // 채팅방 ID
            String chatRoomId = entry.getKey();
            // 해당 채팅방에 접속한 세션들
            Set<String> sessions = entry.getValue();

            if (sessions.contains(sessionId)) {
                sessionTracker.removeSession(chatRoomId, sessionId); // 세션을 제거한다.

                // 인원 수 갱신 전송
                int count = sessionTracker.getSessionCount(chatRoomId);
                // 모든 클라이언트에게 인원수를 전송
                MessageDto.ChatSocketMessage memberCountMsg = new MessageDto.ChatSocketMessage(
                        "MEMBER_COUNT",
                        Long.parseLong(chatRoomId),
                        Map.of("count", count)
                );
                messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, memberCountMsg);

                // 퇴장 메시지 전송
                MessageDto.ChatSocketMessage systemMsg = new MessageDto.ChatSocketMessage(
                        "SYSTEM_MESSAGE",
                        Long.parseLong(chatRoomId),
                        Map.of("message", name + "님이 퇴장했습니다.")
                );
                messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, systemMsg);

            }
        }
    }
}
