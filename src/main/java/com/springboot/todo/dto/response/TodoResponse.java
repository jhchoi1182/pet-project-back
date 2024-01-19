package com.springboot.todo.dto.response;

import com.springboot.todo.dto.TodoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
