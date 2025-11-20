package org.baljaguk.domain.team.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class TeamCreateRequest {

    // 팀 제목
    private String title;

    // 팀 설명/메모
    private String description;

    // 최대 팀원 수
    private int maxMemberCount;

    // 스킬 태그
    private List<String> techStacks;
}
