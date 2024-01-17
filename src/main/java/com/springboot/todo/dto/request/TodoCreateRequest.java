package com.springboot.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TodoCreateRequest {

    private String contents;
    private String dueDate;

}
