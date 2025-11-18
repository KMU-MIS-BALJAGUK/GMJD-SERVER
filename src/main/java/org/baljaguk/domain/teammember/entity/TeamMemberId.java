package org.baljaguk.domain.teammember.entity;

import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TeamMemberId implements Serializable { // ID 클래스는 Serializable 필수

    private Long team; // TeamMember 엔티티의 @Id 필드명과 일치
    private Long user; // TeamMember 엔티티의 @Id 필드명과 일치

    // 복합키는 equals와 hashCode를 꼭 오버라이드해야 합니다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamMemberId that = (TeamMemberId) o;
        return Objects.equals(team, that.team) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, user);
    }
}