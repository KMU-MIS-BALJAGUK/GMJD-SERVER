package org.baljaguk.domain.contest.dto.request;

import org.baljaguk.global.util.customAnnotaion.ValidKeyword;

public record SearchRequest(

        @ValidKeyword
        String keyword

) {
    public String normalizedKeyword() {

        if (keyword == null) {
            return null;    // 검색 조건 없음
        }

        return keyword
                .trim()                 // 앞뒤 공백 제거
                .replaceAll("\\s+", "");  // 공백 제거
    }
}
