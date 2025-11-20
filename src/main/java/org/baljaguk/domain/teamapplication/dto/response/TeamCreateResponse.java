package org.baljaguk.domain.team.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamCreateResponse {

    private Long teamId;
    private String title;
    private Integer maxMember;
    private String status;
    private Long leaderId;
}
