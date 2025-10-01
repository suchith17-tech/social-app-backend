package com.suchith.socialmedia.socialapp.controller;

import com.suchith.socialmedia.socialapp.payload.UserSummary;
import com.suchith.socialmedia.socialapp.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    // Follow a user
    @PostMapping("/{userId}")
    public ResponseEntity<String> follow(@PathVariable Long userId, Authentication authentication) {
        String username = authentication.getName();
        followService.follow(userId, username);
        return ResponseEntity.ok("Followed user with ID: " + userId);
    }

    // Unfollow a user
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> unfollow(@PathVariable Long userId, Authentication authentication) {
        String username = authentication.getName();
        followService.unfollow(userId, username);
        return ResponseEntity.ok("Unfollowed user with ID: " + userId);
    }

    // Get followers of a user
    @GetMapping("/{userId}/followers")
    public List<UserSummary> followers(@PathVariable Long userId) {
        return followService.getFollowers(userId);
    }

    // Get following of a user
    @GetMapping("/{userId}/following")
    public List<UserSummary> following(@PathVariable Long userId) {
        return followService.getFollowing(userId);
    }
}
