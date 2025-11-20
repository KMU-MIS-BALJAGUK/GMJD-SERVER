package org.baljaguk.domain.team.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 공모전의 팀인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

    // 팀장
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_leader_id", nullable = false)
    private User teamLeader;

    // 팀 제목
    @Column(nullable = false, length = 100)
    private String title;

    // 팀 소개 / 메모
    @Column(columnDefinition = "TEXT")
    private String introduction;

    // 최대 인원
    @Column(nullable = false)
    private Integer maxMember;

    // 팀 모집 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TeamStatus status;

    private
}
