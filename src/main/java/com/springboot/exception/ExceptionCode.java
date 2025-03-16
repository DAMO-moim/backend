package com.springboot.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404,"Member not found"),
    MEMBER_EXISTS(409,"Member exists"),
    GROUP_NOT_FOUND(404,"Group not found"),
    GROUP_EXISTS(409,"Group exists"),
    SCHEDULE_NOT_FOUND(404,"Group Schedule not found"),
    SCHEDULE_EXISTS(409,"Group Schedule exists"),
    BOARD_NOT_FOUND(404,"Board not found"),
    BOARD_EXISTS(409,"Board exists"),
    COMMENT_NOT_FOUND(404,"Comment not found"),
    COMMENT_EXISTS(409,"Comment exists"),
    USER_NOT_LOGGED_IN(401, "You are not logged in"),
    LOGOUT_ERROR(409, "logout error");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int statusCode, String message){
        this.message = message;
        this.status = statusCode;
    }
}
