package com.example.demo.core.wc.model;

import com.example.demo.core.openinghour.model.CreateOpeningHourRequest;
import lombok.*;
import lombok.experimental.Accessors;

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
public class CreateWaterClosetRequest {

    private String name;
    private String description;
    private Boolean isFree;
    private Map<String, Object> accessibilityFeatures;
    private LocationCoordinates location;
    private List<UUID> draftPhotoUuids;
    private Set<CreateOpeningHourRequest> openingHours;
}
