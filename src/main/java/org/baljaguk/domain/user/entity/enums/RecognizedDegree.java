package org.baljaguk.domain.user.entity.enums;

public enum RecognizedDegree {

    ASSOCIATE("University (2-3 years)"),
    BACHELOR("University (4 years)");

    private final String displayName;

    RecognizedDegree(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
