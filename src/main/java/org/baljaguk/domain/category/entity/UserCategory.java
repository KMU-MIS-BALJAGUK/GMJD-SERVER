package org.baljaguk.domain.category.entity;

import jakarta.persistence.*;
import lombok.*;
import org.baljaguk.domain.user.entity.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Table(name = "users_categories")
public class UserCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회원 식별자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 카테고리 식별자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * UserCategory 매핑 객체 생성 메서드
     */
    public static UserCategory of(User user, Category category) {
        return UserCategory.builder()
                .user(user)
                .category(category)
                .build();
    }
}
