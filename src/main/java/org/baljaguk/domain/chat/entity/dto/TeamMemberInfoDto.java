package org.baljaguk.domain.chat.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.baljaguk.domain.team.entity.TeamMember;

@Getter
@Builder
@AllArgsConstructor
public class TeamMemberInfoDto {
    private Long userId;
    private String userName;
    private String userProfileUrl;

    public static TeamMemberInfoDto from(TeamMember teamMember) {

        return TeamMemberInfoDto.builder()
                .userId(teamMember.getMember().getId())
                .userName(teamMember.getMember().getName())
                .userProfileUrl(teamMember.getMember().getProfileImageUrl())
                .build();
    }
}
