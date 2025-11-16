package org.baljaguk.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.user.dto.ProfileResponseDto;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.repository.UserRepository;
import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository; // DB 담당 Repository

    /**
     * 본인 프로필 조회
     */
    @Override
    public ProfileResponseDto getMyProfile(Long currentUserId) {

        // 1. (가장 중요) 404 Not Found 예외 처리
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 2. 찾은 User 엔티티를 1단계에서 만든 DTO로 변환합니다.
        return ProfileResponseDto.fromEntity(user);
    }
}
