package com.suchith.socialmedia.socialapp.service;

import com.suchith.socialmedia.socialapp.model.Follow;
import com.suchith.socialmedia.socialapp.model.Post;
import com.suchith.socialmedia.socialapp.model.User;
import com.suchith.socialmedia.socialapp.payload.PostResponse;
import com.suchith.socialmedia.socialapp.payload.UserSummary;
import com.suchith.socialmedia.socialapp.repository.FollowRepository;
import com.suchith.socialmedia.socialapp.repository.PostRepository;
import com.suchith.socialmedia.socialapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public FollowService(FollowRepository followRepository,
                         UserRepository userRepository,
                         PostRepository postRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // follow target userId by current username
    public void follow(Long targetUserId, String currentUsername) {
        User current = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));
        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target user not found"));

        if (current.getId().equals(target.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot follow yourself");
        }

        if (followRepository.existsByFollowerAndFollowing(current, target)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already following");
        }

        Follow f = new Follow();
        f.setFollower(current);
        f.setFollowing(target);
        followRepository.save(f);
    }

    public void unfollow(Long targetUserId, String currentUsername) {
        User current = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));
        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target user not found"));

        if (!followRepository.existsByFollowerAndFollowing(current, target)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not following this user");
        }

        followRepository.deleteByFollowerAndFollowing(current, target);
    }

    @Transactional(readOnly = true)
    public List<UserSummary> getFollowers(Long userId) {
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return followRepository.findByFollowing(target).stream()
                .map(Follow::getFollower)
                .map(u -> new UserSummary(u.getId(), u.getUsername()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserSummary> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return followRepository.findByFollower(user).stream()
                .map(Follow::getFollowing)
                .map(u -> new UserSummary(u.getId(), u.getUsername()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getFeed(String currentUsername) {
        User current = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // get followed users
        List<User> following = followRepository.findByFollower(current)
                .stream().map(Follow::getFollowing).collect(Collectors.toList());

        // include current user's own posts in the feed
        following.add(current);

        // fetch posts in descending order of creation
        List<Post> posts = postRepository.findByUserInOrderByCreatedAtDesc(following);

        return posts.stream()
                .map(p -> new PostResponse(
                        p.getId(),
                        p.getContent(),
                        p.getCreatedAt(),
                        p.getUser().getId(),
                        p.getUser().getUsername()
                ))
                .collect(Collectors.toList());
    }

}
