package com.springboot.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404,"Member not found"),
    MEMBER_EXISTS(409,"Member exists"),
    MEMBER_NOT_OWNER(403, "You are not the owner of this resource"),
    MEMBER_NAME_EXISTS(409, "Member Name exists"),
    MEMBER_PHONE_NUMBER_EXISTS(409, "Member Phone Number exists"),
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
    INVALID_GENDER(400,"성별 조건에 부합하지 않아 가입이 불가능합니다."),
    INVALID_AGE(400,"나이 조건에 부합하지 않아 가입이 불가능합니다."),
    CATEGORY_NOT_FOUND(404,"Category not found"),
    CHAT_NOT_FOUND(404,"ChatRoom not found"),
    MESSAGE_NOT_FOUND(404,"Message nof found"),
    SUBCATEGORY_NOT_FOUND(404, "서브 카테고리를 찾을 수 없습니다."),
    TAG_NOT_FOUND(404, "태그를 찾을 수 없습니다"),
    EXCEED_GROUP_CREATION_LIMIT(400, "최대 3개의 모임까지 생성할 수 있습니다."),
    EXCEED_CATEGORY_GROUP_CREATION_LIMIT(400, "해당 카테고리에서는 최대 3개의 모임까지만 생성할 수 있습니다."),
    EXCEED_GROUP_JOIN_LIMIT(400, "최대 10개의 모임까지 가입할 수 있습니다."),
    NO_MEMBER_TO_DELEGATE(409, "모임에 위임할 수 있는 다른 멤버가 존재하지 않습니다."),
    IMAGE_REQUIRED(400, "아직 이미지를 업로드 하지 않았습니다."),
    SCHEDULE_START_TIME_PAST(400, "일정 시작 시간은 현재 시간 이후여야 합니다."),
    SCHEDULE_START_AFTER_END(400, "일정 시작 시간은 종료 시간보다 빨라야 합니다."),
    RECURRING_SCHEDULE_TOO_SHORT(400, "정기 일정은 최소 7일 이상의 기간이어야 합니다."),
    INVALID_SCHEDULE_CAPACITY(400,"일정 인원 수는 1명 이상이고 모임 최대 인원 이하이어야 합니다."),
    INVALID_SCHEDULE_COUNT(400,"일정 최대 인원 수는 현재 참여중인 인원수보다 낮을 수 없습니다."),
    INVALID_SCHEDULE_DAYOFWEEK(400, "정기 일정에는 최소 1일 이상의 요일이 지정되어야 합니다."),
    INVALID_GROUP_CAPACITY_UPDATE(400, "현재 가입된 인원보다 작은 최대 인원으로 수정할 수 없습니다."),
    INVALID_REFRESH_TOKEN(400, "유효하지 않은 리플래시 토큰입니다."),
    NOT_INTERESTED_CATEGORY(400, "해당 카테고리에 속하지 않습니다."),
    SCHEDULE_NOT_IN_GROUP(400, "해당 일정이 속한 모임이 아닙니다."),
    SCHEDULE_ALREADY_COMPLETED(400, "이 모임일정은 이미 종료되었습니다."),
    MEMBER_ALREADY_JOINED_SCHEDULE(400, "이미 해당 일정에 참여하고 있는 회원입니다."),
    MEMBER_NOT_JOINED_SCHEDULE(400, "해당 일정에 참여하지 않은 회원입니다."),
    LEADER_CANNOT_CANCEL_SCHEDULE(400, "모임장은 일정을 취소할 수 없습니다."),
    CANNOT_DELETE_GROUP_LEADER(400, "모임장은 회원 탈퇴할 수 없습니다."),
    PASSWORD_NOT_MATCHED(400, "기존 비밀번호가 일치하지 않습니다."),
    PASSWORD_SAME_AS_OLD(400, "새 비밀번호는 기존에 사용한 비밀번호와 동일합니다.");



    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int statusCode, String message){
        this.message = message;
        this.status = statusCode;
    }
}
