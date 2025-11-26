package org.baljaguk.domain.chat.repository;

import org.baljaguk.domain.chat.entity.LastReadTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LastReadTimeRepository extends JpaRepository<LastReadTime, Long> {

    Optional<LastReadTime> findByUserIdAndRoomId(long userId, long roomId);
}
