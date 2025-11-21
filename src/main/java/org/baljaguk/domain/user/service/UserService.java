package org.baljaguk.domain.user.service;

import org.baljaguk.domain.user.dto.request.CategoryUpdateRequest;
import org.baljaguk.domain.user.dto.request.EducationUpdateRequest;
import org.baljaguk.domain.user.dto.request.SkillUpdateRequest;
import org.baljaguk.domain.user.dto.request.UserUpdateRequest;
import org.baljaguk.domain.user.dto.response.ProfileResponse;
import org.baljaguk.domain.user.dto.response.UserSkillsResponse;

public interface UserService {
    void updateUserProfile(Long id, UserUpdateRequest request);

    void updateMySkills(Long userId, SkillUpdateRequest request);

    void updateEducation(Long userId, EducationUpdateRequest request);

    void updateInterests(Long userId, CategoryUpdateRequest request);

    ProfileResponse getMyProfile(Long userId);

    UserSkillsResponse getMySkills(Long userId);
}


