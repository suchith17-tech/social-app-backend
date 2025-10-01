package com.suchith.socialmedia.socialapp.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PostRequest {
    @NotNull
    // private Long userId;

    @NotBlank
    private String content;

    /* public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }*/

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
