package com.example.demo.controllers;

import com.example.demo.core.photo.repository.DraftPhotoRepository;
import com.example.demo.core.photo.service.DraftPhotoService;
import com.example.demo.core.photo.service.PhotoService;
import com.example.demo.core.photo.model.SaveDraftPhotoResponse;
import com.example.demo.core.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
public class PhotoController {

    private final DraftPhotoRepository draftPhotoRepository;
    private final DraftPhotoService draftPhotoService;
    private final PhotoService photoService;

    @PostMapping
    public SaveDraftPhotoResponse saveDraftPhoto(@RequestParam("image") MultipartFile file,
                                                 @AuthenticationPrincipal AppUser user) {
        return draftPhotoService.saveDraftPhoto(file, user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/draft/{uuid}")
    public ResponseEntity<?> getImageByName(@PathVariable("uuid") UUID uuid) {
        byte[] image = draftPhotoService.getImage(uuid);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getImageDataByUUID(@PathVariable("uuid") UUID uuid) {
        byte[] image = photoService.getImageDataByUUID(uuid);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

    @DeleteMapping("/{uuid}")
    public void deletePhoto(@PathVariable("uuid") UUID uuid,
                            @AuthenticationPrincipal AppUser user) {
        photoService.deletePhoto(uuid, user);
    }
}
