package org.baljaguk.domain.user.dto.response;

import org.baljaguk.domain.user.entity.User;

import java.util.List;

public record ProfileResponse(
        String profileImageUrl,
        String name,
        String introduction,
        Integer level,
        String email,
        String universityName,
        String major,
        List<String> skillList, // 스킬셋
        List<String> categoryList // 관심분야
) {
    public static ProfileResponse of(User user) {

        List<String> skillList = (user.getSkills() != null && !user.getSkills().isEmpty())
                ? List.of(user.getSkills().split(","))
                : List.of();

        List<String> categoryList = user.getCategories()
                .stream()
                .filter(uc -> uc.getCategory() != null)
                .map(uc -> uc.getCategory().getName())
                .toList();

        return new ProfileResponse(
                user.getProfileImageUrl(),
                user.getName(),
                user.getIntroduction(),
                user.getLevel(),
                user.getEmail(),
                user.getUniversityName(),
                user.getMajor(),
                skillList,
                categoryList
        );
    }
}
