package org.baljaguk.domain.user.service;

import jakarta.validation.Valid;
import org.baljaguk.domain.user.dto.request.*;
import org.baljaguk.domain.user.dto.response.ProfileResponse;
import org.baljaguk.domain.user.dto.response.UserSkillsResponse;

public interface UserService {
    void updateUserProfile(Long id, UserUpdateRequest request);

    void updateMySkills(Long userId, SkillUpdateRequest request);

    void updateEducation(Long userId, EducationUpdateRequest request);

    void updateInterests(Long userId, CategoryUpdateRequest request);

    ProfileResponse getMyProfile(Long userId);

    UserSkillsResponse getMySkills(Long userId);

    void updateIntroduction(Long userId, @Valid IntroductionUpdateRequest request);
}


