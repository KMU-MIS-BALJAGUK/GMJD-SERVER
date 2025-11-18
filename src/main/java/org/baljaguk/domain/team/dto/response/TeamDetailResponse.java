package org.baljaguk.domain.team.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 팀 상세 정보를 응답으로 내려주는 DTO
 */
@Getter
@AllArgsConstructor
public class TeamDetailResponse {

    /** 팀 ID */
    private Long teamId;

    /** 팀 이름 */
    private String name;

    /** 팀 설명 */
    private String description;

    /** 최대 인원 수 */
    private Integer maxMembers;

    /** 현재 팀 인원 수 */
    private Integer currentMembers;

    /** 팀 상태 (예: OPEN, CLOSE 등) */
    private String status;
}
