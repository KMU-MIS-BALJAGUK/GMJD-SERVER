package org.baljaguk.domain.chat.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TeamMemberInfoDto {
    private Long userId;
    private String userName;
    private String userProfileUrl;
}
