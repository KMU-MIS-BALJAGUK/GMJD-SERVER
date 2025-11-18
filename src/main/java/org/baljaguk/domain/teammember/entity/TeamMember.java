package org.baljaguk.domain.teammember.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.user.entity.User;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "team_member") // DB 스키마의 테이블 이름
@IdClass(TeamMemberId.class) // (중요!) 방금 1/5에서 만든 ID 클래스(TeamMemberId) 지정
public class TeamMember {

    @Id // 복합키 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id") // DB의 team_id 컬럼
    private Team team;

    @Id // 복합키 2
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // DB의 user_id 컬럼
    private User user;

    @CreationTimestamp // INSERT 시 자동으로 현재 시간 저장
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt; // DB의 joined_at 컬럼
}