package com.example.demo.core.wc.model;

import com.example.demo.core.openinghour.model.OpeningHourResponse;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class WaterClosetResponse {
    private UUID id;
    private String name;
    private String description;
    private Boolean isFree;
    private Map<String, Object> accessibilityFeatures;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private LocationCoordinates location;
    private Set<OpeningHourResponse> openingHours;
    private List<UUID> photos;
    private UUID createdBy;
}
