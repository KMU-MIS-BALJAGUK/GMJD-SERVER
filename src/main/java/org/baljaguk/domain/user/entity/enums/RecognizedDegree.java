package org.baljaguk.domain.user.entity.enums;

public enum RecognizedDegree {

    ASSOCIATE("대학교 (2,3년)"),
    BACHELOR("대학교 (4년)"),
    MASTER("대학원");

    private final String displayName;

    RecognizedDegree(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
