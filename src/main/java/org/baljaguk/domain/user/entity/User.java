package org.baljaguk.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "level")
    private String level;

    @Column(name = "birthdate")
    private String birthdate;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "university")
    private String university;

    @Column(name = "major")
    private String major;

    @ElementCollection // List<String>을 저장하기 위한 표준 JPA 어노테이션
    @CollectionTable(name = "user_skills", joinColumns = @JoinColumn(name = "user_id")) // 스킬을 저장할 별도 테이블
    @Column(name = "skill") //
    private List<String> skills;

    public static User createSocialUser(String email, String name, String profileImageUrl) {
        return User.builder()
                .email(email)
                .name(name)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
