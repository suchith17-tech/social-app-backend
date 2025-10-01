package com.suchith.socialmedia.socialapp.controller;

import com.suchith.socialmedia.socialapp.payload.PostRequest;
import com.suchith.socialmedia.socialapp.payload.PostResponse;
import com.suchith.socialmedia.socialapp.payload.UpdatePostRequest;
import com.suchith.socialmedia.socialapp.service.PostService;
import com.suchith.socialmedia.socialapp.service.LikeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final LikeService likeService;

    public PostController(PostService postService, LikeService likeService) {
        this.postService = postService;
        this.likeService = likeService;
    }

    // ✅ Create Post
    @PostMapping
    public ResponseEntity<PostResponse> create(
            @Valid @RequestBody PostRequest req,
            Authentication authentication
    ) {
        String username = authentication.getName(); // comes from JWT
        PostResponse saved = postService.create(req, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ✅ Get all posts
    @GetMapping
    public List<PostResponse> all() {
        return postService.getAll();
    }

    // ✅ Get one post by ID
    @GetMapping("/{id}")
    public PostResponse one(@PathVariable Long id) {
        return postService.getOne(id);
    }

    // ✅ Get posts by user
    @GetMapping("/user/{userId}")
    public List<PostResponse> byUser(@PathVariable Long userId) {
        return postService.getByUser(userId);
    }

    // ✅ Update a post
    @PutMapping("/{id}")
    public PostResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest req,
            Authentication authentication
    ) {
        String username = authentication.getName();
        return postService.update(id, req, username);
    }


    // ✅ Delete a post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        postService.delete(id, username);
        return ResponseEntity.noContent().build();
    }


}
