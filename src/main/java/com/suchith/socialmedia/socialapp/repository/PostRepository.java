package com.suchith.socialmedia.socialapp.repository;

import com.suchith.socialmedia.socialapp.model.Post;
import com.suchith.socialmedia.socialapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // ✅ All posts, newest first
    List<Post> findAllByOrderByCreatedAtDesc();

    // ✅ Posts by single user, newest first
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

    // ✅ Posts by a list of users (for feed), newest first
    List<Post> findByUserInOrderByCreatedAtDesc(List<User> users);
}
