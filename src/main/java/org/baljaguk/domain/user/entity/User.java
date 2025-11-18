package org.baljaguk.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.baljaguk.domain.user.entity.enums.Education;
import org.baljaguk.domain.user.entity.enums.RecognizedDegree;

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

    @Column(name = "profile_image_url", nullable = false)
    private String profileImageUrl;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "introduction", nullable = false)
    private String introduction;

    @Column(name = "education", nullable = false)
    @Enumerated(EnumType.STRING)
    private Education education;  // 학력

    @Column(name = "recognized_degree", nullable = false)
    @Enumerated(EnumType.STRING)
    private RecognizedDegree recognizedDegree;

    @Column(name = "interests", nullable = false)
    private String interests;  // 관심분야는 콤마로 나누어 저장 및 응답합니다.

    @Column(name = "level", nullable = true)
    private Integer level;

    @Column(name = "birthdate", nullable = false)
    private String birthdate;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "university_name", nullable = false)
    private String universityName;

    @Column(name = "major", nullable = false)
    private String major;

    @Column(name = "skill", nullable = false)
    private String skills;  // 스킬셋은 콤마로 나누어 저장 및 응답합니다.

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
                .introduction("")                      // 기본 소개
                .birthdate("")                         // 기본 생년월일 공란
                .universityName("")                    // 기본 대학교명 공란
                .major("")                             // 기본 전공명 공란
                .skills("")                            // 스킬셋 공란
                .interests("")                         // 관심분야 공란
                .education(null)                       // 학력은 이후 선택
                .recognizedDegree(null)                // 인정학력도 이후 선택
                .build();
    }

    // 자체 회원가입 업데이트
    public void updateUserProfile(String introduction, String universityName, String major, String skills,
                                  String interests, Education education, RecognizedDegree recognizedDegree) {
        this.introduction = introduction;
        this.universityName = universityName;
        this.major = major;
        this.skills = skills;
        this.interests = interests;
        this.education = education;
        this.recognizedDegree = recognizedDegree;
    }
}
