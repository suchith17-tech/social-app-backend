package com.suchith.socialmedia.socialapp.payload;

import jakarta.validation.constraints.NotBlank;

public class UpdatePostRequest {

    @NotBlank(message = "Content must not be blank")
    private String content;

    // Getter + Setter
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

