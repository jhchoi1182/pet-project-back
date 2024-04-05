package com.springboot.petProject.dto.request.post;

import com.springboot.petProject.types.PostCategory;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostCreateRequest {

    private PostCategory category;
    @Size(min = 1, message = "Post should have atleast 1 characters")
    private String title;
    @Size(min = 1, message = "Post should have atleast 1 characters")
    private String contents;
    private List<String> images;

}
