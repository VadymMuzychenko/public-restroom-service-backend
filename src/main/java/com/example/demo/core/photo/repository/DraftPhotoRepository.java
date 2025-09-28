package com.example.demo.core.photo.repository;

import com.example.demo.core.photo.model.DraftPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DraftPhotoRepository extends JpaRepository<DraftPhoto, UUID> {
}
