package com.example.demo.core.photo.repository;

import com.example.demo.core.photo.model.Photo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PhotoRepository extends JpaRepository<Photo, UUID> {
    List<Photo> findByWaterCloset_Id(UUID waterClosetId, Sort sort);

    List<Photo> getPhotosByComment_Id(UUID commentId, Sort sort);
}
