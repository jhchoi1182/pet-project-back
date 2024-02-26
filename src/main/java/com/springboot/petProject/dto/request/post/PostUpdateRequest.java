package com.springboot.petProject.dto.request.post;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostUpdateRequest {

    @Size(min = 1, message = "Post should have atleast 1 characters")
    private String title;
    @Size(min = 1, message = "Post should have atleast 1 characters")
    private String contents;

}
