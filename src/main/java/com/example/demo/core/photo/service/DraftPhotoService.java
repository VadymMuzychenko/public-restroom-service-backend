package com.example.demo.core.photo.service;

import com.example.demo.core.photo.model.DraftPhoto;
import com.example.demo.core.photo.model.SaveDraftPhotoResponse;
import com.example.demo.core.photo.repository.DraftPhotoRepository;
import com.example.demo.core.user.model.AppUser;
import com.example.demo.utils.ImageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DraftPhotoService {

    private final DraftPhotoRepository draftPhotoRepository;

    public SaveDraftPhotoResponse saveDraftPhoto(MultipartFile file, AppUser user) {
        try {
            DraftPhoto photoToSave = new DraftPhoto();
            photoToSave.setImageData(com.example.demo.utils.ImageUtil.compressImage(file.getBytes()));
            photoToSave.setExpiresAt(OffsetDateTime.now().plusMonths(1));
            photoToSave.setUploadedAt(OffsetDateTime.now());
            photoToSave.setUploadedBy(user);
            DraftPhoto saved = draftPhotoRepository.save(photoToSave);
            return new SaveDraftPhotoResponse(saved.getId());
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    @Transactional
    public byte[] getImage(UUID uuid) {
        Optional<DraftPhoto> dbImage = draftPhotoRepository.findById(uuid);
        byte[] image = ImageUtil.decompressImage(dbImage.get().getImageData());
        return image;
    }

    public DraftPhoto getPhoto(UUID uuid) {
        Optional<DraftPhoto> dbImage = draftPhotoRepository.findById(uuid);
        return dbImage.get();
    }

    public void removeDraftPhoto(UUID draftPhotoId) {
        draftPhotoRepository.deleteById(draftPhotoId);
    }

}
