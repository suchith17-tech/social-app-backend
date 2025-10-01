package com.suchith.socialmedia.socialapp.repository;

import com.suchith.socialmedia.socialapp.model.Like;
import com.suchith.socialmedia.socialapp.model.Post;
import com.suchith.socialmedia.socialapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // Check if user already liked post
    boolean existsByUserAndPost(User user, Post post);

    // Remove a like
    void deleteByUserAndPost(User user, Post post);

    // Count likes for a post
    long countByPost(Post post);
}
