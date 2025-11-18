package org.baljaguk.domain.user.service;

import org.baljaguk.domain.user.dto.request.UserUpdateRequest;

public interface UserService {
    void updateUserProfile(Long id, UserUpdateRequest request);
}
