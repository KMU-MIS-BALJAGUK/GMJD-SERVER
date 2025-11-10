package org.baljaguk.domain.user.repository;

import org.baljaguk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
