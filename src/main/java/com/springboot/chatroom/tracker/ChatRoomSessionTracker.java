package com.springboot.chatroom.tracker;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatRoomSessionTracker {
    // 각 채팅방별 접속한 세션들을 저장 (roomId -> [sessionId, sessionId...])
    private final Map<String, Set<String>> roomSessions = new ConcurrentHashMap<>();
    // 각 세션 ID에 해당하는 사용자 이름 저장 (sessionId -> username)
    private final Map<String, String> sessionToUsername = new ConcurrentHashMap<>();
    //구독 중복 방지를 위한 객체
    private final Set<String> joinedRooms = ConcurrentHashMap.newKeySet();

    //새로운 구독이 발생하면 호출
    public boolean addSession(String roomId, String sessionId, String username) {
        String key = sessionId + ":" + roomId;
        if (!joinedRooms.add(key)) {
            // 이미 같은 세션이 같은 방에 구독한 경우니까 무시한다.
            return false;
        }
        //해당 채팅방에 세션 등록
        Set<String> sessions = roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet());
        sessions.add(sessionId);
        //세션에 사용자 이메일 매핑
        sessionToUsername.put(sessionId, username);
        return true;
    }

    // 세션이 끊기거나, 채팅방에서 퇴장했을때 호출
    public boolean removeSession(String roomId, String sessionId) {
        Set<String> sessions = roomSessions.get(roomId);
        boolean removed = false;
        if (sessions != null) {
            removed = sessions.remove(sessionId); //세션 제거
            if (sessions.isEmpty()) {
                roomSessions.remove(roomId); //세션이 없으면 방 제거(세션 추척을 없앤다는 의미)
            }
        }
        // 사용자 정보, 중복 추적을 제거한다
        sessionToUsername.remove(sessionId);
        joinedRooms.remove(sessionId + ":" + roomId);
        return removed;
    }

    //현재 채팅방 접속 인원 수 반환
    public int getSessionCount(String roomId) {
        return roomSessions.getOrDefault(roomId, Collections.emptySet()).size();
    }

    //세션 ID로 사용자 이름 가져오기
    public String getUsername(String sessionId) {
        return sessionToUsername.get(sessionId);
    }

    //전체 세션 상태 (디버깅용 or 퇴장 처리용)
    public Map<String, Set<String>> getAllSessions() {
        return roomSessions;
    }
}