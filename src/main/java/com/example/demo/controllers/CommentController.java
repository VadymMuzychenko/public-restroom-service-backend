package com.example.demo.controllers;

import com.example.demo.core.PagingList;
import com.example.demo.core.comment.CommentService;
import com.example.demo.core.comment.model.CommentResponse;
import com.example.demo.core.comment.model.CreateCommentRequest;
import com.example.demo.core.comment.model.EditCommentRequest;
import com.example.demo.core.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/water-closet")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{wcUuid}/comment")
    public CommentResponse addComment(@PathVariable("wcUuid") UUID wcUuid,
                                      @RequestBody CreateCommentRequest request,
                                      @AuthenticationPrincipal AppUser user) {
        return commentService.addComment(request, wcUuid, user);
    }

    @GetMapping("/{wcUuid}/comment")
    public PagingList<CommentResponse> getComments(@PathVariable("wcUuid") UUID wcUuid,
                                                   @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return commentService.getWaterClosetComments(wcUuid, pageNumber, pageSize);
    }

    @PostMapping("/comment/edit") // TODO - path
    public CommentResponse editComment(@PathVariable("wcUuid") UUID wcUuid, EditCommentRequest request) {
        return commentService.editComment(request);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") UUID commentId,
                                           @AuthenticationPrincipal AppUser user) {
        commentService.deleteComment(commentId, user);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/comment/{commentId}")
    public ResponseEntity<?> deleteCommentForAdmin(@PathVariable("commentId") UUID commentId) {
        commentService.deleteCommentForAdmin(commentId);
        return ResponseEntity.ok().build();
    }
}
