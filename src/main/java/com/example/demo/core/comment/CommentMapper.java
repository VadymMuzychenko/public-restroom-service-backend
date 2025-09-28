package com.example.demo.core.comment;

import com.example.demo.core.comment.model.Comment;
import com.example.demo.core.comment.model.CommentResponse;

import java.util.List;
import java.util.UUID;

public class CommentMapper {

    public static CommentResponse toCommentResponse(Comment comment, List<UUID> photos) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setRating(comment.getRating());
        response.setUserData(
                new CommentResponse.UserData(
                        comment.getUser().getFirstName(),
                        comment.getUser().getLastName()
                )
        );
        response.setWaterClosetId(comment.getWaterCloset().getId());
        response.setCreatedAt(comment.getCreatedAt());
        response.setPhotos(photos);
        return response;
    }
}
