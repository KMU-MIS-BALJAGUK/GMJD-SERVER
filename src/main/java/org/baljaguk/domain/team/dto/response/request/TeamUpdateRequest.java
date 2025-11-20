package org.baljaguk.domain.team.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TeamUpdateRequest {

    private String title;
    private String description;
    private Integer maxMemberCount;
    private List<String> techStacks;
}
