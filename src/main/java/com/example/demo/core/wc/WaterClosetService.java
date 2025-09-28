package com.example.demo.core.wc;


import com.example.demo.core.openinghour.OpeningHourService;
import com.example.demo.core.openinghour.model.OpeningHourResponse;
import com.example.demo.core.photo.service.PhotoService;
import com.example.demo.core.wc.model.CreateWaterClosetRequest;
import com.example.demo.core.wc.model.WaterCloset;
import com.example.demo.core.wc.model.WaterClosetMapResponse;
import com.example.demo.core.wc.model.WaterClosetResponse;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.core.user.model.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WaterClosetService {

    private final WaterClosetRepository waterClosetRepository;
    private final PhotoService photoService;
    private final ObjectMapper objectMapper;
    private final OpeningHourService openingHourService;

    public WaterClosetResponse createWaterCloset(CreateWaterClosetRequest request, AppUser user) {
        WaterCloset waterCloset = new WaterCloset();
        waterCloset.setName(request.getName());
        waterCloset.setDescription(request.getDescription());
        waterCloset.setName(request.getName());
        waterCloset.setDescription(request.getDescription());
        waterCloset.setIsFree(request.getIsFree());
        waterCloset.setAccessibilityFeatures(request.getAccessibilityFeatures());
        waterCloset.setCreatedAt(OffsetDateTime.now());
        waterCloset.setUpdatedAt(OffsetDateTime.now());
        waterCloset.setCreatedBy(user);
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(request.getLocation().getLongitude(), request.getLocation().getLatitude()));
        point.setSRID(4326);
        waterCloset.setLocation(point);
        waterCloset.setStatus("PENDING");
        WaterCloset saved = waterClosetRepository.save(waterCloset);
        List<UUID> photosUuids = null;
        if (request.getDraftPhotoUuids() != null && !request.getDraftPhotoUuids().isEmpty()) {
            photosUuids = photoService.moveDraftPhotosToWCPhotos(request.getDraftPhotoUuids(), saved);
        }
        Set<OpeningHourResponse> openingHourResponseSet = null;
        if (request.getOpeningHours() != null && !request.getOpeningHours().isEmpty()) {
            openingHourResponseSet = openingHourService.addOpeningHours(request.getOpeningHours(), saved);
        }
        return WaterClosetMapper.toWaterClosetResponse(saved, photosUuids, openingHourResponseSet);
    }

    public WaterClosetResponse getWaterCloset(UUID wcUuid) {
        Optional<WaterCloset> optional = waterClosetRepository.findById(wcUuid);
        if (optional.isEmpty()) {
            throw new NotFoundException("WaterCloset not found");
        }
        WaterCloset waterCloset = optional.get();
        List<UUID> photoUuids = photoService.getPhotoUUIDsByWC(wcUuid);
        Set<OpeningHourResponse> openingHours = openingHourService.getOpeningHours(wcUuid);
        return WaterClosetMapper.toWaterClosetResponse(waterCloset, photoUuids, openingHours);
    }

    public List<WaterClosetMapResponse> getWaterClosetsWithinRadius(Double latitude, Double longitude, Double radius) {
        List<WaterCloset> list = waterClosetRepository.findWaterClosetsWithinRadius(longitude, latitude, radius);
        return list.stream().map(WaterClosetMapper::toWaterClosetMapResponse).toList();
    }

    public void deleteWaterCloset(UUID uuid) {
        waterClosetRepository.deleteById(uuid);
    }
}
