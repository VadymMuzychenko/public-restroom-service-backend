package com.example.demo.core.photo.service;

import com.example.demo.core.photo.model.Photo;
import com.example.demo.core.photo.repository.PhotoRepository;
import com.example.demo.core.wc.model.WaterCloset;
import com.example.demo.core.comment.model.Comment;
import com.example.demo.exceptions.ForbiddenException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.core.user.model.AppUser;
import com.example.demo.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final TransactionalPhotoMover transactionalPhotoMover;
    private final PhotoRepository photoRepository;

    public List<UUID> moveDraftPhotosToWCPhotos(List<UUID> uuids, WaterCloset waterCloset) {
        ListIterator<UUID> iterator = uuids.listIterator();
        ArrayList<UUID> newUUIDs = new ArrayList<>();
        boolean setPrimary = false;
        while (iterator.hasNext()) {
            UUID uuid = iterator.next();
            if (!setPrimary) {
                newUUIDs.add(transactionalPhotoMover.movePhotoFromDraftToWCPhoto(uuid, true, waterCloset));
                setPrimary = true;
            } else {
                newUUIDs.add(transactionalPhotoMover.movePhotoFromDraftToWCPhoto(uuid, false, waterCloset));
            }
        }
        return newUUIDs;
    }

    public List<UUID> moveDraftPhotosToCommentPhotos(List<UUID> uuids, Comment comment) {
        ListIterator<UUID> iterator = uuids.listIterator();
        ArrayList<UUID> newUUIDs = new ArrayList<>();
        boolean setPrimary = false;
        while (iterator.hasNext()) {
            UUID uuid = iterator.next();
            if (!setPrimary) {
                newUUIDs.add(transactionalPhotoMover.movePhotoFromDraftToCommentPhoto(uuid, true, comment));
                setPrimary = true;
            } else {
                newUUIDs.add(transactionalPhotoMover.movePhotoFromDraftToCommentPhoto(uuid, false, comment));
            }
        }
        return newUUIDs;
    }

    public List<UUID> getPhotoUUIDsByWC(UUID wcUuid) {
        List<Photo> photos = photoRepository.findByWaterCloset_Id(wcUuid,
                Sort.by(Sort.Order.asc("isPrimary")));
        return photos.stream().map(Photo::getId).toList();
    }

    public byte[] getImageDataByUUID(UUID uuid) {
        Optional<Photo> dbImage = photoRepository.findById(uuid);
        byte[] image = ImageUtil.decompressImage(dbImage.get().getImageData());
        return image;
    }

    public List<UUID> getPhotoUuidsByComment(UUID commentUuid) {
        List<Photo> photos = photoRepository.getPhotosByComment_Id(commentUuid,
                Sort.by(Sort.Order.asc("isPrimary")));
        return photos.stream().map(Photo::getId).toList();
    }

    public void deletePhoto(UUID uuid, AppUser author) {
        Optional<Photo> optional = photoRepository.findById(uuid);
        if (optional.isEmpty()) {
            throw new NotFoundException("Photo with id " + uuid + " not found");
        }
        Photo photo = optional.get();
        if (!photo.getWaterCloset().getCreatedBy().equals(author)) {
            throw new ForbiddenException("The user is not the author of the photo");
        }
        photoRepository.deleteById(uuid);
    }
}
