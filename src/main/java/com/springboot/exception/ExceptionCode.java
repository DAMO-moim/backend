package com.springboot.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404,"Member not found"),
    MEMBER_EXISTS(409,"Member exists"),
    MEMBER_NOT_OWNER(403, "You are not the owner of this resource"),
    GROUP_NOT_FOUND(404,"Group not found"),
    GROUP_EXISTS(409,"Group exists"),
    SCHEDULE_NOT_FOUND(404,"Group Schedule not found"),
    SCHEDULE_EXISTS(409,"Group Schedule exists"),
    BOARD_NOT_FOUND(404,"Board not found"),
    BOARD_EXISTS(409,"Board exists"),
    COMMENT_NOT_FOUND(404,"Comment not found"),
    FORBIDDEN_OPERATION(403, "You are not allowed to create a board"),
    COMMENT_EXISTS(409,"Comment exists"),
    USER_NOT_LOGGED_IN(401, "You are not logged in"),
    LOGOUT_ERROR(409, "logout error"),
    ACCESS_DENIED(403, "접근 권한이 없습니다."),
    UNAUTHORIZED_ACCESS(403, "관리자 권한이 없습니다."),
    UNAUTHORIZED_MEMBER_ACCESS(401,"Not authorized to access this resource"),
    INVALID_MEMBER_COUNT(400, "모임 인원 수는 최소 2명, 최대 100명으로 설정해야 합니다."),
    MEMBER_NOT_GROUP_LEADER(400, "모임장이 아닙니다."),
    MEMBER_NOT_FOUND_IN_GROUP(401, "모임 내 회원을 찾을 수 없습니다"),
    MEMBER_NOT_IN_GROUP(403, "회원이 모임에 가입한 상태가 아님"),
    MEMBER_ALREADY_JOINED_GROUP(409, "이미 모임에 가입한 회원입니다."),
    GROUP_FULL(400, "모임의 최대 인원 수를 초과했습니다."),
    INVALID_CREDENTIALS(400,"비밀번호 또는 이메일이 틀렸습니다."),
    CATEGORY_NOT_FOUND(404,"Category not found"),
    CHAT_NOT_FOUND(404,"ChatRoom not found"),
    MESSAGE_NOT_FOUND(404,"Message nof found"),
    SUBCATEGORY_NOT_FOUND(404, "서브 카테고리를 찾을 수 없습니다.");


    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int statusCode, String message){
        this.message = message;
        this.status = statusCode;
    }
}
