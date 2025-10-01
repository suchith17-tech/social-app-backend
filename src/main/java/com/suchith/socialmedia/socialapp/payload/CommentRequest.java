package com.suchith.socialmedia.socialapp.payload;

import jakarta.validation.constraints.NotBlank;

public class CommentRequest {
    @NotBlank
    private String content;

    // getter + setter
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
