package com.example.demo.core.wc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WaterClosetMapResponse {
    private UUID id;
    private String name;
    private String description;
    private Boolean isFree;
    private LocationCoordinates location;
}
