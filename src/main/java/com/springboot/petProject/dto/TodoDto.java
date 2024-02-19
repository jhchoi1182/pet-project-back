package com.springboot.petProject.dto;

import com.springboot.petProject.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class TodoDto {

    private Integer todoId;
    private String contents;
    private Boolean isDone;
    private String dueDate;
    private UserDto user;
    private Timestamp registeredAt;
    private Timestamp updatedAt;

    public static TodoDto fromEntity(Todo entity) {
        return new TodoDto(
                entity.getId(),
                entity.getContents(),
                entity.getIsDone(),
                entity.getDueDate(),
                UserDto.fromEntity(entity.getUser()),
                entity.getRegisteredAt(),
                entity.getUpdatedAt()
        );
    }

}
