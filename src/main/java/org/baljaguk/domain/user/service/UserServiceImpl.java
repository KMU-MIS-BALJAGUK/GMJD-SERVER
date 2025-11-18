package org.baljaguk.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.user.dto.request.UserUpdateRequest;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.repository.UserRepository;
import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.handler.UserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void updateUserProfile(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        String skillsAsString = request.skills() != null ? String.join(",", request.skills()) : "";
        String interestsAsString = request.interests() != null ? String.join(",", request.interests()) : "";

        user.updateUserProfile(
                request.introduction(),
                request.universityName(),
                request.major(),
                skillsAsString,
                interestsAsString,
                request.education(),
                request.recognizedDegree()
        );
    }
}
