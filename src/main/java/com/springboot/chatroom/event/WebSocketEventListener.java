package com.springboot.chatroom.event;

import com.springboot.chatroom.config.StompHandler;
import com.springboot.chatroom.tracker.ChatRoomSessionTracker;
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

    // 사용자가 채팅방에 입장할 때 호출(클라이언트가 구독시)
    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage()); //STOMP 헤더 추출
        String destination = accessor.getDestination();  // "/sub/chat/1" 등의 구독 대상 결로를 가져온다.
        String sessionId = accessor.getSessionId(); // 웹 소켓과 연결된 클라이언트의 세션 ID

        if (destination != null && destination.startsWith("/sub/chat/")) {
            //구독 주소에서 채팅방 ID 추출
            String chatRoomId = destination.split("/")[3];
            //세션에서 사용자 정보 추출
            String username = StompHandler.getUsernameBySessionId(sessionId);

            //세션과 채팅방 연결을 추척
            sessionTracker.addSession(chatRoomId, sessionId, username);

            // 인원 수 갱신 브로드캐스트
            // 현재 채팅방 인원 수 가져오기
            int count = sessionTracker.getSessionCount(chatRoomId);
            // 모든 클라이언트에게 인원수를 전송
            messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId + "/members", count);

            // 입장 메시지 출력
            messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId,
                    Map.of("system", true, "message", username + "님이 입장했습니다."));
        }
    }

    // 사용자가 채팅방에서 퇴장했을 때(클라이언트가 WebSocket 연결을 끊었을 때 호출)
    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        // 끊어진 세션 id
        String sessionId = event.getSessionId();
        // 해당 세션에 연결된 사용자 정보 추출
        String username = sessionTracker.getUsername(sessionId);

        // 모든 채팅방에 대해 세션을 찾아서 제거
        for (Map.Entry<String, Set<String>> entry : sessionTracker.getAllSessions().entrySet()) {
            // 채팅방 ID
            String roomId = entry.getKey();
            // 해당 채팅방에 접속한 세션들
            Set<String> sessions = entry.getValue();

            if (sessions.contains(sessionId)) {
                sessionTracker.removeSession(roomId, sessionId); // 세션을 제거한다.

                // 인원 수 갱신 전송
                int count = sessionTracker.getSessionCount(roomId);
                messagingTemplate.convertAndSend("/sub/chat/" + roomId + "/members", count);

                // 퇴장 메시지 전송
                messagingTemplate.convertAndSend("/sub/chat/" + roomId,
                        Map.of("system", true, "message", username + "님이 퇴장했습니다."));
            }
        }
    }
}
