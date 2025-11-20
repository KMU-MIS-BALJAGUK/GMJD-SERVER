package org.baljaguk.domain.team.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TeamCreateResponse {

    private Long teamId;
    private Long contestId;
    private String title;
}
