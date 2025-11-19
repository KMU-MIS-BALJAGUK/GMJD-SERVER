package org.baljaguk.domain.category.repository;

import org.baljaguk.domain.category.entity.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.baljaguk.domain.user.entity.User;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {

    void deleteAllByUser(User user);

}
