package com.example.demo.photo;

import com.example.demo.core.photo.service.PhotoService;
import com.example.demo.core.photo.service.TransactionalPhotoMover;
import com.example.demo.core.wc.model.WaterCloset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PhotoServiceTest {

    @InjectMocks
    public PhotoService photoService;
    @Mock
    public TransactionalPhotoMover transactionalPhotoMover;

    @Test
    public void testMovePhotoFromDraftToWCPhoto() {
        MockitoAnnotations.openMocks(this);
        List<UUID> uuids = new ArrayList<>();
        uuids.add(UUID.randomUUID());
        uuids.add(UUID.randomUUID());
        uuids.add(UUID.randomUUID());
        when(transactionalPhotoMover.movePhotoFromDraftToWCPhoto(any(), anyBoolean(), any())).thenReturn(UUID.randomUUID());
        List<UUID> result = photoService.moveDraftPhotosToWCPhotos(uuids, new WaterCloset());
        Assertions.assertEquals(uuids.size(), result.size());
        verify(transactionalPhotoMover, times(1)).movePhotoFromDraftToWCPhoto(any(), eq(true), any());
        verify(transactionalPhotoMover, times(2)).movePhotoFromDraftToWCPhoto(any(), eq(false), any());
    }
}
