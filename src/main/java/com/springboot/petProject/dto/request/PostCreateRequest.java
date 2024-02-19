package com.springboot.petProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCreateRequest {

    private String contents;
    private String dueDate;

}
