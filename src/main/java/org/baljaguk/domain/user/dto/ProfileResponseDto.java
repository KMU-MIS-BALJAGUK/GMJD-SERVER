package org.baljaguk.domain.user.dto;

import org.baljaguk.domain.user.entity.User; // User 엔터티 경로

import java.time.LocalDate;
import java.util.List;

public record ProfileResponseDto(
        Long userId,
        String profileImageUrl,
        String name,
        String introduction,
        String level,
        LocalDate birthdate,
        String email,
        String university,
        String major,
        List<String> skills // User 엔티티에 skills 필드가 있음
) {

    public static ProfileResponseDto fromEntity(User user) {

        return new ProfileResponseDto(
                user.getId(),
                user.getProfileImageUrl(),
                user.getName(),
                user.getIntroduction(),
                user.getLevel(),
                user.getBirthdate(),
                user.getEmail(),
                user.getUniversity(),
                user.getMajor(),
                user.getSkills()
        );
    }
}