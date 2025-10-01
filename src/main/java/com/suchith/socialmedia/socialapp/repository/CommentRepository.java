package com.suchith.socialmedia.socialapp.repository;

import com.suchith.socialmedia.socialapp.model.Comment;
import com.suchith.socialmedia.socialapp.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
