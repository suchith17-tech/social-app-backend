package com.suchith.socialmedia.socialapp.service;

import com.suchith.socialmedia.socialapp.model.Comment;
import com.suchith.socialmedia.socialapp.model.Post;
import com.suchith.socialmedia.socialapp.model.User;
import com.suchith.socialmedia.socialapp.payload.CommentRequest;
import com.suchith.socialmedia.socialapp.payload.CommentResponse;
import com.suchith.socialmedia.socialapp.payload.UpdateCommentRequest;
import com.suchith.socialmedia.socialapp.repository.CommentRepository;
import com.suchith.socialmedia.socialapp.repository.PostRepository;
import com.suchith.socialmedia.socialapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository,
                          UserRepository userRepository,
                          PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public CommentResponse create(Long postId, CommentRequest req, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        Comment c = new Comment();
        c.setContent(req.getContent());
        c.setUser(user);
        c.setPost(post);
        c.setCreatedAt(java.time.LocalDateTime.now());

        Comment saved = commentRepository.save(c);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        return commentRepository.findByPost(post).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse update(Long postId, Long commentId, UpdateCommentRequest req, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!comment.getPost().getId().equals(postId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment does not belong to this post");
        }

        if (!comment.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You may only edit your own comments");
        }

        comment.setContent(req.getContent());
        Comment saved = commentRepository.save(comment);
        return toResponse(saved);
    }

    public void delete(Long postId, Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!comment.getPost().getId().equals(postId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment does not belong to this post");
        }

        if (!comment.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You may only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    private CommentResponse toResponse(Comment c) {
        CommentResponse r = new CommentResponse();
        r.setId(c.getId());
        r.setContent(c.getContent());
        r.setUserId(c.getUser().getId());
        r.setUsername(c.getUser().getUsername());
        r.setCreatedAt(c.getCreatedAt());
        return r;
    }
}
