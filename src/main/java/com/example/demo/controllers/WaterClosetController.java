package com.example.demo.controllers;

import com.example.demo.core.photo.service.PhotoService;
import com.example.demo.core.wc.WaterClosetRepository;
import com.example.demo.core.wc.WaterClosetService;
import com.example.demo.core.wc.model.CreateWaterClosetRequest;
import com.example.demo.core.wc.model.WaterClosetMapResponse;
import com.example.demo.core.wc.model.WaterClosetResponse;
import com.example.demo.core.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/water-closet")
@RequiredArgsConstructor
public class WaterClosetController {

    private final WaterClosetRepository waterClosetRepository;
    private final WaterClosetService waterClosetService;
    private final PhotoService photoService;

    @PostMapping
    public ResponseEntity<WaterClosetResponse> createWaterCloset(@RequestBody CreateWaterClosetRequest request,
                                                                 @AuthenticationPrincipal AppUser user) {

        WaterClosetResponse response = waterClosetService.createWaterCloset(request, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<WaterClosetResponse> getWaterCloset(@PathVariable UUID uuid) {
        return ResponseEntity.ok(waterClosetService.getWaterCloset(uuid));
    }

    @GetMapping("/get-all-within-range")
    public ResponseEntity<List<WaterClosetMapResponse>> getWaterCloset(@RequestParam("latitude") Double latitude,
                                                                       @RequestParam("longitude") Double longitude,
                                                                       @RequestParam("range") Double range) {
        return ResponseEntity.ok(waterClosetService.getWaterClosetsWithinRadius(latitude, longitude, range));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteWaterCloset(@PathVariable UUID uuid) {
        waterClosetService.deleteWaterCloset(uuid);
        return ResponseEntity.ok().build();
    }
}
