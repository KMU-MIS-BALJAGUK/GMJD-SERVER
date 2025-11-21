package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.team.entity.Question;
import org.baljaguk.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTeam(Team team);
}
