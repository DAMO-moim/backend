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
    INVALID_CREDENTIALS(401,"잘못된 이메일 또는 비밀번호입니다."),
    UNAUTHORIZED_MEMBER_ACCESS(401,"Not authorized to access this resource"),
    CATEGORY_NOT_FOUND(404,"Category not found");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int statusCode, String message){
        this.message = message;
        this.status = statusCode;
    }
}
