package org.baljaguk.domain.teamapplication.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*; // (Spring Boot 3.x 이상 기준)
// import javax.persistence.*; // (Spring Boot 2.x 기준)
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.baljaguk.domain.team.entity.Team;
import org.baljaguk.domain.user.entity.User;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "team_application") // DB 스키마의 테이블 이름
public class TeamApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 스키마의 id (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team; // 스키마의 team_id (FK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 스키마의 user_id (FK)

    @Column(length = 255) // 스키마의 comment
    private String comment;

    @Column(name = "selected_skills") // 스키마의 selected_skills
    private String selectedSkills; // (JSON/Tag를 String으로 처리)

    @Column(length = 50, nullable = false) // 스키마의 status
    private String status; // (e.g. "대기중", "수락", "거절")

    @CreationTimestamp
    @Column(name = "applied_at", nullable = false, updatable = false)
    private LocalDateTime appliedAt; // 스키마의 applied_at
}