package org.baljaguk.domain.contest.repository;

import org.baljaguk.domain.contest.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    @Query("""
        SELECT c FROM Contest c
        WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.organizationName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.companyType) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Contest> searchByKeyword(String keyword);

    List<Contest> findAll();
}
