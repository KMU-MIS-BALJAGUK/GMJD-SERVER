package org.baljaguk.domain.user.entity.enums;

public enum Education {
    HIGH_SCHOOL("고등학교"),
    UNIVERSITY("대학교");

    private final String displayName;

    Education(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
