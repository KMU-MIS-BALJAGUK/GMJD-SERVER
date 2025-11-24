package org.baljaguk.domain.team.entity;

import lombok.Getter;

@Getter
public enum TeamStatus {
    OPEN("모집중"),
    CLOSED("모집완료"),
    EXPIRED("만료됨");;

    private final String displayName;

    TeamStatus(String displayName) {
        this.displayName = displayName;
    }
}
