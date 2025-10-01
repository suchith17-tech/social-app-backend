package com.suchith.socialmedia.socialapp.service;

import com.suchith.socialmedia.socialapp.model.Like;
import com.suchith.socialmedia.socialapp.model.Post;
import com.suchith.socialmedia.socialapp.model.User;
import com.suchith.socialmedia.socialapp.repository.LikeRepository;
import com.suchith.socialmedia.socialapp.repository.PostRepository;
import com.suchith.socialmedia.socialapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;  // ✅ Added
import org.springframework.web.server.ResponseStatusException;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // ✅ Like a post
    @Transactional
    public void likePost(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (likeRepository.existsByUserAndPost(user, post)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You already liked this post");
        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);

        likeRepository.save(like);
    }

    // ✅ Unlike a post
    @Transactional   // 👈 required for delete
    public void unlikePost(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (!likeRepository.existsByUserAndPost(user, post)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You haven’t liked this post yet");
        }

        likeRepository.deleteByUserAndPost(user, post);
    }

    // ✅ Count likes
    @Transactional(readOnly = true)   // 👈 optimization: only read
    public long countLikes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        return likeRepository.countByPost(post);
    }
}
