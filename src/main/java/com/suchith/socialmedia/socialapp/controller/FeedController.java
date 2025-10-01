package com.suchith.socialmedia.socialapp.controller;

import com.suchith.socialmedia.socialapp.payload.PostResponse;
import com.suchith.socialmedia.socialapp.service.FollowService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FeedController {

    private final FollowService followService;

    public FeedController(FollowService followService) { this.followService = followService; }

    @GetMapping("/feed")
    public List<PostResponse> feed(Authentication authentication) {
        String username = authentication.getName();
        return followService.getFeed(username);
    }
}
