package com.example.demo.core.photo.model;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavePhotoRequest {
    private byte[] imageData;
}
