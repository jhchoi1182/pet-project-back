package com.springboot.todo.dto.response;

import com.springboot.todo.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserSignupResponse {

    private Integer id;
    private String username;

    public static UserSignupResponse from(UserDto user) {
        return new UserSignupResponse(
                user.getId(),
                user.getUsername()
        );
    }

}
