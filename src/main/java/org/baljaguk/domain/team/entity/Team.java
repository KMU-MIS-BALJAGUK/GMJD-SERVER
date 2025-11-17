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
@Builder
@Table(name = "teams")
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "introduction", nullable = false)
    private String introduction;

    @Column(name = "max_member", nullable = false)
    private Integer maxMember;

    @Column(name = "status", nullable = false)
    private TeamStatus status;

    @Column(name = "memo", nullable = true)
    private String memo;

    @Column(name = "question", nullable = false)
    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_leader_id", nullable = false)
    private User teamLeader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;
}
