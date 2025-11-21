package org.baljaguk.domain.team.repository;

import org.baljaguk.domain.team.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
