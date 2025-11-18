package org.baljaguk.domain.team.entity;

import org.baljaguk.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String introduction;

    private Integer maxMember;

    @Enumerated(EnumType.STRING)
    private TeamStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private User teamLeader;

}
