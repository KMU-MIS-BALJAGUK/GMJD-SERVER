package org.baljaguk.domain.team.entity;

import jakarta.persistence.*;
import lombok.*;
import org.baljaguk.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Table(name = "team_apply")
public class TeamApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RegisterStatus status;

    @Column(name = "skills", nullable = true)
    private String skills; // 스킬셋은 ,로 나누어 저장

    @Column(name = "ai_tags", nullable = true)
    private String aiTags; // 태그는 ,로 나누어 저장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @OneToMany(mappedBy = "teamApply", fetch = FetchType.LAZY)
    private List<Answer> answer = new ArrayList<>();

    // 정팩메
    public static TeamApply create(User user,
                                   Team team,
                                   String skills) {

        return TeamApply.builder()
                .status(RegisterStatus.REQUESTED) // 신청은 기본 REQUESTED
                .skills(skills)                   // "Java,SpringBoot" 형식
                .user(user)
                .team(team)
                .build();
    }

    public void updateAiTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            this.aiTags = null;
        } else {
            this.aiTags = String.join(",", tags);
        }
    }

    public void setStatus(RegisterStatus status) {
        this.status = status;
    }
}
