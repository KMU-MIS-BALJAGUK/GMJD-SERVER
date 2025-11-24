package org.baljaguk.domain.team.entity;

import lombok.Getter;

@Getter
public enum TeamMemberType {
    MEMBER("팀원"),
    LEADER("팀장");

    private final String displayName;

    TeamMemberType(String displayName) {
        this.displayName = displayName;
    }
}
