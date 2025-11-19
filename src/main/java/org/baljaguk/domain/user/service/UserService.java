package org.baljaguk.domain.user.service;

import org.baljaguk.domain.user.dto.request.UserUpdateRequest;
import org.baljaguk.domain.user.dto.response.ProfileResponse;

public interface UserService {
    void updateUserProfile(Long id, UserUpdateRequest request);

    ProfileResponse getMyProfile(Long userId);
}
