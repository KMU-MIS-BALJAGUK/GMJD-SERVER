package org.baljaguk.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="last_read_time", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","room_id"}))
public class LastReadTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id",nullable = false)
    private Long userId;


    @Column(name="room_id",nullable = false)
    private Long roomId;

    @Column(name="last_read_at")
    private LocalDateTime lastReadAt;

    public void updateLastReadTime() {
        this.lastReadAt = LocalDateTime.now();
    }
}
