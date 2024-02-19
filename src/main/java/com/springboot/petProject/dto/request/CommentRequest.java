package com.springboot.petProject.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequest {

    @Size(min = 1, max = 200, message = "Post should have between 1 and 200 characters")
    private String comment;

}
