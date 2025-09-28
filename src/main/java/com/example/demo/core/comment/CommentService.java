package com.example.demo.core.comment;

import com.example.demo.core.PagingList;
import com.example.demo.core.comment.model.Comment;
import com.example.demo.core.comment.model.CommentResponse;
import com.example.demo.core.comment.model.CreateCommentRequest;
import com.example.demo.core.comment.model.EditCommentRequest;
import com.example.demo.core.photo.repository.PhotoRepository;
import com.example.demo.core.photo.service.PhotoService;
import com.example.demo.core.wc.WaterClosetRepository;
import com.example.demo.core.wc.WaterClosetService;
import com.example.demo.core.wc.model.WaterCloset;
import com.example.demo.exceptions.ForbiddenException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.core.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final WaterClosetService waterClosetService;
    private final WaterClosetRepository waterClosetRepository;
    private final PhotoService photoService;
    private final CommentRepository commentRepository;
    private final PhotoRepository photoRepository;

    @Transactional
    public CommentResponse addComment(CreateCommentRequest request, UUID wcUuid, AppUser user) {
        Optional<WaterCloset> optional = waterClosetRepository.findById(wcUuid);
        if (optional.isEmpty()) {
            throw new NotFoundException("Water closet with id " + wcUuid + " not found");
        }
        WaterCloset wc = optional.get();
        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setRating(request.getRating());
        comment.setUser(user);
        comment.setWaterCloset(wc);
        comment.setCreatedAt(OffsetDateTime.now());
        Comment saved = commentRepository.save(comment);
        if (request.getDraftPhotoUuids() != null && !request.getDraftPhotoUuids().isEmpty()) {
            List<UUID> movedUuids = photoService.moveDraftPhotosToCommentPhotos(request.getDraftPhotoUuids(), saved);
            return CommentMapper.toCommentResponse(saved, movedUuids);
        } else {
            return CommentMapper.toCommentResponse(saved, null);
        }
    }

    public PagingList<CommentResponse> getWaterClosetComments(UUID wcUuid, int pageNumber, int pageSize) {
        Optional<WaterCloset> optional = waterClosetRepository.findById(wcUuid);
        if (optional.isEmpty()) {
            throw new NotFoundException("Water closet with id " + wcUuid + " not found");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Comment> page = commentRepository.getCommentsByWaterCloset_Id(wcUuid, pageable);
        List<CommentResponse> responseList = page.stream().map(c -> {
            List<UUID> photos = photoService.getPhotoUuidsByComment(c.getId());
            return CommentMapper.toCommentResponse(c, photos);
        }).toList();
        return new PagingList<>(pageNumber, page.getTotalPages(), page.getTotalElements(), responseList);
    }

    public CommentResponse editComment(EditCommentRequest request) {
        Optional<Comment> optional = commentRepository.findById(request.getId());
        if (optional.isEmpty()) {
            throw new NotFoundException("Comment with id " + request.getId() + " not found");
        }
        Comment comment = optional.get();
        comment.setText(request.getText());
        comment.setRating(request.getRating());
        Comment saved = commentRepository.save(comment);
        List<UUID> photos = photoService.getPhotoUuidsByComment(saved.getId());
        return CommentMapper.toCommentResponse(saved, photos);
    }

    public void deleteComment(UUID commentUuid, AppUser author) {
        Optional<Comment> optional = commentRepository.findById(commentUuid);
        if (optional.isEmpty()) {
            throw new NotFoundException("Comment with id " + commentUuid + " not found");
        }
        Comment comment = optional.get();
        if (!comment.getUser().equals(author)) {
            throw new ForbiddenException("The user is not the author of the comment");
        }
        commentRepository.delete(comment);
    }

    public void deleteCommentForAdmin(UUID commentUuid) {
        Optional<Comment> optional = commentRepository.findById(commentUuid);
        if (optional.isEmpty()) {
            throw new NotFoundException("Comment with id " + commentUuid + " not found");
        }
        Comment comment = optional.get();
        commentRepository.delete(comment);
    }
}
