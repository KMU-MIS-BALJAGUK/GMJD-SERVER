package org.baljaguk.domain.team.dto.response.enums;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팀 신청 가능 여부 상태값")
public enum CanApply {

    @Schema(description = "신청 가능")
    OK,

    @Schema(description = "본인이 팀장이라 신청 불가")
    TEAM_LEADER,

    @Schema(description = "이미 이 팀에 신청한 상태")
    ALREADY_APPLIED,

    @Schema(description = "동일 공모전의 다른 팀에 이미 신청함")
    APPLIED_IN_OTHER_TEAM,

    @Schema(description = "동일 공모전의 팀에 이미 가입된 상태")
    ALREADY_MEMBER,

    @Schema(description = "로그인이 필요한 상태")
    NOT_LOGIN;
}
