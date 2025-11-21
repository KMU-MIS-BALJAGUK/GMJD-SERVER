package org.baljaguk.domain.team.entity;

import jakarta.persistence.*;
import lombok.*;
import org.baljaguk.domain.contest.entity.Contest;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.global.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "team")
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 팀 제목
    @Column(nullable = false, length = 100)
    private String title;

    // 팀 소개 / 메모
    @Column(columnDefinition = "TEXT")
    private String introduction;

    // 최대 인원
    @Column(nullable = false)
    private Integer maxMember;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamStatus status;

    @Column(nullable = true)
    private String memo;

    // 어떤 공모전의 팀인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

    // 팀장
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_leader_id", nullable = false)
    private User teamLeader;

    // 정팩메
    public static Team create(String title,
                              String introduction,
                              Integer maxMember,
                              Contest contest,
                              User teamLeader) {

        Team team = new Team();
        team.title = title;
        team.introduction = introduction;
        team.maxMember = maxMember;
        team.status = TeamStatus.OPEN; // 기본값
        team.contest = contest;
        team.teamLeader = teamLeader;
        return team;
    }
}
