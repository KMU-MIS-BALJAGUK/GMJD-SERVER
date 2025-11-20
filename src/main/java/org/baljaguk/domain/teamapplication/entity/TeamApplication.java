package org.baljaguk.domain.teamapplication.entity;

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
@Table(name = "team_application")
public class TeamApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 255)
    private String comment;

    @Column(name = "selected_skills")
    private String selectedSkills;

    @Column(length = 50, nullable = false)
    private String status;

    @CreationTimestamp
    @Column(name = "applied_at", nullable = false, updatable = false)
    private LocalDateTime appliedAt;


    public static TeamApplication create(
            Team team,
            User user,
            String comment,
            String selectedSkills
    ) {
        TeamApplication application = new TeamApplication();
        application.team = team;
        application.user = user;
        application.comment = comment;
        application.selectedSkills = selectedSkills;
        application.status = "대기중";
        return application;
    }
}
