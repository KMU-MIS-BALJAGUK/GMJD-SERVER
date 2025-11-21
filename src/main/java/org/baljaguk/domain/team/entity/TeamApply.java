package org.baljaguk.domain.team.entity;

import jakarta.persistence.*;
import lombok.*;
import org.baljaguk.domain.user.entity.User;
import org.baljaguk.domain.user.entity.enums.RegisterStatus;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Table(name = "team_apply")
class TeamApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    private RegisterStatus status;

    @Column(name = "answer", nullable = false)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
}
