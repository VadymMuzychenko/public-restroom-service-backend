package com.example.demo.core.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  CommentResponse {

    private UUID id;
    private UUID waterClosetId;
    private UserData userData;

    private String text;
    private Short rating;
    private OffsetDateTime createdAt;
    private List<UUID> photos;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserData {
        private String firstName;
        private String lastName;
    }
}
