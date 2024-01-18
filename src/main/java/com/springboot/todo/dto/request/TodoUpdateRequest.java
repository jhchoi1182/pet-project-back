package com.springboot.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TodoUpdateRequest {

    private String contents;
    private String dueDate;

}
