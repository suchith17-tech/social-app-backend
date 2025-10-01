package com.suchith.socialmedia.socialapp.repository;

import com.suchith.socialmedia.socialapp.model.Follow;
import com.suchith.socialmedia.socialapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);

    void deleteByFollowerAndFollowing(User follower, User following);

    List<Follow> findByFollower(User follower);

    List<Follow> findByFollowing(User following);
}
