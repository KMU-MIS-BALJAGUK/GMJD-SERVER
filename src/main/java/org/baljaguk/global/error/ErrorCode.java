package org.baljaguk.global.error;

public enum ErrorCode {

    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    TEAM_NOT_FOUND("팀을 찾을 수 없습니다."),
    TEAM_NOT_RECRUITING("팀이 모집 중이 아닙니다."),
    TEAM_IS_FULL("팀 정원이 초과되었습니다."),
    CANNOT_APPLY_TO_OWN_TEAM("자신의 팀에는 신청할 수 없습니다."),
    ALREADY_TEAM_MEMBER("이미 팀원입니다."),
    ALREADY_APPLIED("이미 신청한 팀입니다."),
    ALREADY_CREATED_TEAM("이미 생성한 팀이 존재합니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
