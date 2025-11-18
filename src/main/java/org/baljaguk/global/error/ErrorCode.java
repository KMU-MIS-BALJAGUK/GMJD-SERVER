package org.baljaguk.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 404 NOT_FOUND
    TEAM_NOT_FOUND(404, "T001", "해당 팀을 찾을 수 없습니다."),
    USER_NOT_FOUND(404, "U001", "해당 유저를 찾을 수 없습니다."),

    // 400 BAD_REQUEST
    ALREADY_TEAM_MEMBER(400, "T002", "이미 가입된 팀원입니다."),
    ALREADY_APPLIED(400, "T003", "이미 신청하여 승인 대기 중입니다."),
    TEAM_IS_FULL(400, "T004", "팀 인원이 모두 모집되었습니다."),
    TEAM_NOT_RECRUITING(400, "T005", "현재 모집 중인 팀이 아닙니다."),
    CANNOT_APPLY_TO_OWN_TEAM(400, "T006", "팀장은 자신의 팀에 신청할 수 없습니다.");



    private final int status;       // HTTP 상태 코드
    private final String code;      // 어플리케이션 내부 식별 코드
    private final String message;   // 사용자에게 보여줄 메시지
}