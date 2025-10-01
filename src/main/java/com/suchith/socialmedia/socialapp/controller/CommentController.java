package com.suchith.socialmedia.socialapp.controller;

import com.suchith.socialmedia.socialapp.payload.CommentRequest;
import com.suchith.socialmedia.socialapp.payload.CommentResponse;
import com.suchith.socialmedia.socialapp.payload.UpdateCommentRequest;
import com.suchith.socialmedia.socialapp.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;
    public CommentController(CommentService commentService) { this.commentService = commentService; }

    // Create comment (auth required)
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> create(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest req,
            Authentication authentication
    ) {
        String username = authentication.getName();
        CommentResponse saved = commentService.create(postId, req, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // List comments (public)
    @GetMapping("/{postId}/comments")
    public List<CommentResponse> list(@PathVariable Long postId) {
        return commentService.getByPost(postId);
    }

    // Update comment (auth + owner)
    @PutMapping("/{postId}/comments/{commentId}")
    public CommentResponse update(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest req,
            Authentication authentication
    ) {
        String username = authentication.getName();
        return commentService.update(postId, commentId, req, username);
    }

    // Delete comment (auth + owner)
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        commentService.delete(postId, commentId, username);
        return ResponseEntity.noContent().build();
    }
}
