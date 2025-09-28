package com.example.demo.core.photo.service;

import com.example.demo.core.photo.model.DraftPhoto;
import com.example.demo.core.photo.model.Photo;
import com.example.demo.core.comment.model.Comment;
import com.example.demo.core.photo.repository.PhotoRepository;
import com.example.demo.core.wc.model.WaterCloset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionalPhotoMover {

    private final PhotoRepository photoRepository;
    private final DraftPhotoService draftPhotoService;

    @Transactional
    public UUID movePhotoFromDraftToWCPhoto(UUID draftUUID, boolean isPrimary, WaterCloset waterCloset) {
        DraftPhoto draftPhoto = draftPhotoService.getPhoto(draftUUID);
        UUID savedUuid = savePhoto(draftPhoto, isPrimary, waterCloset);
        draftPhotoService.removeDraftPhoto(draftUUID);
        return savedUuid;
    }

    private UUID savePhoto(DraftPhoto draftPhoto, boolean isPrimary, WaterCloset waterCloset) {
        Photo photoToSave = new Photo();
        photoToSave.setImageData(draftPhoto.getImageData());
        photoToSave.setIsPrimary(isPrimary);
        photoToSave.setWaterCloset(waterCloset);
        photoToSave.setUploadedAt(draftPhoto.getUploadedAt());
        Photo saved = photoRepository.save(photoToSave);
        return saved.getId();
    }

    @Transactional
    public UUID movePhotoFromDraftToCommentPhoto(UUID draftUUID, boolean isPrimary, Comment comment) {
        DraftPhoto draftPhoto = draftPhotoService.getPhoto(draftUUID);
        UUID savedUuid = savePhoto(draftPhoto, isPrimary, comment);
        draftPhotoService.removeDraftPhoto(draftUUID);
        return savedUuid;
    }

    private UUID savePhoto(DraftPhoto draftPhoto, boolean isPrimary, Comment comment) {
        Photo photoToSave = new Photo();
        photoToSave.setImageData(draftPhoto.getImageData());
        photoToSave.setIsPrimary(isPrimary);
        photoToSave.setComment(comment);
        photoToSave.setUploadedAt(draftPhoto.getUploadedAt());
        Photo saved = photoRepository.save(photoToSave);
        return saved.getId();
    }
}
