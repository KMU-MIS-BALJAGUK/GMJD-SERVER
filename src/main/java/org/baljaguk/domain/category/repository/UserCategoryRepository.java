package org.baljaguk.domain.category.repository;

import org.baljaguk.domain.category.entity.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
}
