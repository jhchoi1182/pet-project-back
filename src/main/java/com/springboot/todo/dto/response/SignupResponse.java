package com.springboot.todo.dto.response;

import com.springboot.todo.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignupResponse {

    private Integer id;
    private String username;

    public static SignupResponse from(UserDto user) {
        return new SignupResponse(
                user.getId(),
                user.getUsername()
        );
    }

}
