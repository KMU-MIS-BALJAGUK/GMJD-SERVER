package org.baljaguk.domain.user.dto.response;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record UserSkillsResponse(
        List<String> skills
) {

    // 정팩메
    public static UserSkillsResponse from(String skillsCsv) {

        if (skillsCsv == null || skillsCsv.isBlank()) {
            return new UserSkillsResponse(Collections.emptyList());
        }

        List<String> skillList = Arrays.stream(skillsCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        return new UserSkillsResponse(skillList);
    }
}
