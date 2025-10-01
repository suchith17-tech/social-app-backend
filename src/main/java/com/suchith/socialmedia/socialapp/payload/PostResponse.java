package com.suchith.socialmedia.socialapp.payload;

import java.time.LocalDateTime;

public class PostResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private String username;

    public PostResponse() {}
    public PostResponse(Long id, String content, LocalDateTime createdAt, Long userId, String username) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.userId = userId;
        this.username = username;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
