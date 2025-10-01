package com.suchith.socialmedia.socialapp.service;

import com.suchith.socialmedia.socialapp.model.Post;
import com.suchith.socialmedia.socialapp.model.User;
import com.suchith.socialmedia.socialapp.payload.PostRequest;
import com.suchith.socialmedia.socialapp.payload.PostResponse;
import com.suchith.socialmedia.socialapp.payload.UpdatePostRequest;
import com.suchith.socialmedia.socialapp.repository.PostRepository;
import com.suchith.socialmedia.socialapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // Create
    public PostResponse create(PostRequest req, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Post post = new Post();
        post.setContent(req.getContent());
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());

        Post saved = postRepository.save(post);
        return toResponse(saved);
    }



    // Read all (newest first)
    public List<PostResponse> getAll() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).toList();
    }

    // Read one
    public PostResponse getOne(Long id) {
        Post p = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return toResponse(p);
    }

    // Read by user
    public List<PostResponse> getByUser(Long userId) {
        // optional: verify user exists
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).toList();
    }

    // Update content only
    public PostResponse update(Long id, UpdatePostRequest req, String username) {
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        // ✅ Ownership check
        if (!existing.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own posts");
        }

        existing.setContent(req.getContent());
        Post saved = postRepository.save(existing);

        return toResponse(saved);
    }



    // ✅ Delete post with ownership check
    public void delete(Long id, String username) {
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        // Prevent others from deleting
        if (!existing.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own posts");
        }

        postRepository.delete(existing);
    }



    // Mapper
    private PostResponse toResponse(Post p) {
        return new PostResponse(
                p.getId(),
                p.getContent(),
                p.getCreatedAt(),
                p.getUser().getId(),
                p.getUser().getUsername()
        );
    }
}
