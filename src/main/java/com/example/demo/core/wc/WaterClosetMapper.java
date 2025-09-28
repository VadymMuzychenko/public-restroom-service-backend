package com.example.demo.core.wc;

import com.example.demo.core.openinghour.model.OpeningHourResponse;
import com.example.demo.core.wc.model.LocationCoordinates;
import com.example.demo.core.wc.model.WaterCloset;
import com.example.demo.core.wc.model.WaterClosetMapResponse;
import com.example.demo.core.wc.model.WaterClosetResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class WaterClosetMapper {
    public static WaterClosetMapResponse toWaterClosetMapResponse(WaterCloset waterCloset) {
        WaterClosetMapResponse wcResponse = new WaterClosetMapResponse();
        wcResponse.setId(waterCloset.getId());
        wcResponse.setName(waterCloset.getName());
        wcResponse.setDescription(waterCloset.getDescription());
        wcResponse.setIsFree(waterCloset.getIsFree());
        LocationCoordinates locationCoordinates = new LocationCoordinates();
        locationCoordinates.setLatitude(waterCloset.getLocation().getY());
        locationCoordinates.setLongitude(waterCloset.getLocation().getX());
        wcResponse.setLocation(locationCoordinates);
        return wcResponse;
    }

    public static WaterClosetResponse toWaterClosetResponse(WaterCloset waterCloset, List<UUID> photosUuids, Set<OpeningHourResponse> openingHours) {
        WaterClosetResponse wcResponse = new WaterClosetResponse();
        wcResponse.setId(waterCloset.getId());
        wcResponse.setName(waterCloset.getName());
        wcResponse.setDescription(waterCloset.getDescription());
        wcResponse.setIsFree(waterCloset.getIsFree());
        LocationCoordinates locationCoordinates = new LocationCoordinates();
        locationCoordinates.setLatitude(waterCloset.getLocation().getY());
        locationCoordinates.setLongitude(waterCloset.getLocation().getX());
        wcResponse.setLocation(locationCoordinates);
        wcResponse.setAccessibilityFeatures(waterCloset.getAccessibilityFeatures());
        wcResponse.setPhotos(photosUuids);
        wcResponse.setOpeningHours(openingHours);
        wcResponse.setCreatedAt(waterCloset.getCreatedAt());
        wcResponse.setUpdatedAt(waterCloset.getUpdatedAt());
        wcResponse.setCreatedBy(waterCloset.getCreatedBy().getId());
        return wcResponse;
    }
}
