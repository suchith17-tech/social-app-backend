package com.suchith.socialmedia.socialapp.controller;

import com.suchith.socialmedia.socialapp.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // ✅ Like a post
    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        likeService.likePost(postId, username);
        return ResponseEntity.ok("Post liked successfully");
    }

    // ✅ Unlike a post (same path, different HTTP method)
    @DeleteMapping("/{postId}/like")
    public ResponseEntity<String> unlikePost(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        likeService.unlikePost(postId, username);
        return ResponseEntity.ok("Post unliked successfully");
    }

    // ✅ Count likes
    @GetMapping("/{postId}/likes")
    public ResponseEntity<Long> countLikes(@PathVariable Long postId) {
        long count = likeService.countLikes(postId);
        return ResponseEntity.ok(count);
    }
}

