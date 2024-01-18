package com.springboot.todo.dto.response;

import com.springboot.todo.dto.TodoDto;
import com.springboot.todo.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class TodoResponse {

    private Integer todoId;
    private String contents;
    private Boolean isDone;
    private String dueDate;

    public static TodoResponse fromDto(TodoDto todo) {
        return new TodoResponse(
                todo.getTodoId(),
                todo.getContents(),
                todo.getIsDone(),
                todo.getDueDate()
        );
    }

}
