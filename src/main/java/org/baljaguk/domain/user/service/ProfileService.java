package org.baljaguk.domain.user.service;

import org.baljaguk.domain.user.dto.ProfileResponseDto;

// "class"가 아니라 "interface" (설계도)입니다.
public interface ProfileService {

    /**
     * 본인 프로필 조회 (설계도)
     * - 실제 코드는 Impl 파일에 있습니다.
     */
    ProfileResponseDto getMyProfile(Long currentUserId);

}