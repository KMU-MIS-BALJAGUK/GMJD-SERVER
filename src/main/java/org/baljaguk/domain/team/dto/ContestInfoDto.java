package org.baljaguk.domain.team.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContestInfoDto {
    private Long contestId;
    private Long teamId;
    private String contestName;
    private String imageUrl;

    @QueryProjection
    public ContestInfoDto(Long contestId, Long teamId, String contestName, String imageUrl) {
        this.contestId = contestId;
        this.teamId = teamId;
        this.contestName = contestName;
        this.imageUrl = imageUrl;
    }
}
