package com.example.demo.core.wc.model;

import com.example.demo.core.photo.model.Photo;
import com.example.demo.core.user.model.AppUser;
import com.example.demo.core.openinghour.model.OpeningHour;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "water_closet")
public class WaterCloset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ColumnDefault("true")
    @Column(name = "is_free", nullable = false)
    private Boolean isFree = false;

    @Column(name = "accessibility_features")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> accessibilityFeatures;

    @ColumnDefault("'PENDING'")
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @ColumnDefault("now()")
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "water_closet_id")
    private Set<OpeningHour> openingHours = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "water_closet_id")
    private Set<Photo> photos = new LinkedHashSet<>();

    @Column(columnDefinition = "geography(Point,4326)", nullable = false)
    private Point location;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private AppUser createdBy;

}