package org.baljaguk.domain.contest.dto.request;

import org.baljaguk.global.util.customAnnotaion.ValidKeyword;

public record SearchRequest(

        @ValidKeyword
        String keyword

) {
    public String normalizedKeyword() {
        return keyword
                .trim()                 // 앞뒤 공백 제거
                .replaceAll("\\s+", "");  // 공백 제거
    }
}
