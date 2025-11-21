package org.baljaguk.domain.user.repository;

import org.baljaguk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.categories uc
        LEFT JOIN FETCH uc.category c
        WHERE u.id = :userId
    """)
    Optional<User> findUserWithCategories(Long userId);
}
