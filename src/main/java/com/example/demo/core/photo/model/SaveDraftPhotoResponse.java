package com.example.demo.core.photo.model;

import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveDraftPhotoResponse {

    private UUID draftPhotoId;
}
