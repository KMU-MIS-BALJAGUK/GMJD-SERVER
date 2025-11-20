package org.baljaguk.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.baljaguk.domain.category.entity.UserCategory;
import org.baljaguk.domain.user.dto.request.UserUpdateRequest;
import org.baljaguk.domain.user.entity.enums.Education;
import org.baljaguk.domain.user.entity.enums.RecognizedDegree;

import java.util.ArrayList;
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

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "profile_image_url", nullable = false)
    private String profileImageUrl;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "introduction", nullable = true)
    private String introduction;

    @Column(name = "education", nullable = true)
    @Enumerated(EnumType.STRING)
    private Education education;  // 학력

    @Column(name = "recognized_degree", nullable = true)
    @Enumerated(EnumType.STRING)
    private RecognizedDegree recognizedDegree;

    @Column(name = "university_name", nullable = true)
    private String universityName;

    @Column(name = "major", nullable = true)
    private String major;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "skill", nullable = true)
    private String skills;  // 스킬셋은 콤마로 나누어 저장 및 응답합니다.

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserCategory> categories = new ArrayList<>();

    /**
     * 소셜 로그인 사용자 생성 (최초 회원가입 시)
     * - email / name / profileImageUrl은 소셜에서 내려오는 값
     * - introduction, level 등은 기본값 세팅
     */
    public static User createSocialUser(String email, String name, String profileImageUrl) {
        return User.builder()
                .email(email)
                .name(name)
                .profileImageUrl(profileImageUrl)
                .level(1)                              // 기본 레벨
                .build();
    }

    // 자체 회원가입 업데이트
    public void updateUserProfile(UserUpdateRequest request) {

        this.introduction = request.introduction();
        this.universityName = request.universityName();
        this.major = request.major();

        // skills(List<String>) → String CSV 저장
        if (request.skills() != null && !request.skills().isEmpty()) {
            this.skills = String.join(",", request.skills());
        } else {
            this.skills = null;
        }

        this.education = request.education();
        this.recognizedDegree = request.recognizedDegree();
    }


    // 스킬셋 수정 메서드
    public void updateSkills(java.util.List<String> newSkills) {
        if (newSkills == null || newSkills.isEmpty()) {
            this.skills = null;
        } else {
            this.skills = newSkills.stream()
                    .collect(java.util.stream.Collectors.joining(","));
        }
    }

    public void updateEducation(String universityName, String major, Education education, RecognizedDegree recognizedDegree) {
        this.universityName = universityName;
        this.major = major;
        this.education = education;
        this.recognizedDegree = recognizedDegree;
    }
}
