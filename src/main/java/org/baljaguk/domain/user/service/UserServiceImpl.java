package org.baljaguk.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.category.entity.Category;
import org.baljaguk.domain.category.entity.UserCategory;
import org.baljaguk.domain.category.repository.CategoryRepository;
import org.baljaguk.domain.category.repository.UserCategoryRepository;
import org.baljaguk.domain.user.dto.request.UserUpdateRequest;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.repository.UserRepository;
import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.handler.CategoryException;
import org.baljaguk.global.api.handler.UserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserCategoryRepository userCategoryRepository;

    @Override
    @Transactional
    public void updateUserProfile(Long userId, UserUpdateRequest request) {
        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        // 2. 유저 기본 정보 업데이트
        user.updateUserProfile(request);

        // 4. 새 매핑 생성 및 저장
        if (request.categoryIds() != null && !request.categoryIds().isEmpty()) {
            List<UserCategory> userCategories = request.categoryIds().stream()
                    .map(categoryId -> {
                        Category category = categoryRepository.findById(categoryId)
                                .orElseThrow(() -> new CategoryException(ErrorCode.NOT_FOUND_CATEGORY));
                        return UserCategory.of(user, category);
                    })
                    .toList();

            userCategoryRepository.saveAll(userCategories);
        }
    }
}
