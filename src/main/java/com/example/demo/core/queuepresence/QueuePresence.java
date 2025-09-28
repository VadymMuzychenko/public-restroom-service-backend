package com.example.demo.core.queuepresence;


import com.example.demo.core.user.model.AppUser;
import com.example.demo.core.wc.model.WaterCloset;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "queue_presence")
public class QueuePresence {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "water_closet_id", nullable = false)
    private WaterCloset waterCloset;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ColumnDefault("now()")
    @Column(name = "entered_at", nullable = false)
    private OffsetDateTime enteredAt;

    @ColumnDefault("now()")
    @Column(name = "last_seen_at", nullable = false)
    private OffsetDateTime lastSeenAt;

}