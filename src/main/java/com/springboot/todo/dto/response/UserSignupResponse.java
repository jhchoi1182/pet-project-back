package com.springboot.todo.dto.response;

import com.springboot.todo.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserSignupResponse {

    private Integer userId;
    private String username;

    public static UserSignupResponse fromDto(UserDto user) {
        return new UserSignupResponse(
                user.getUserId(),
                user.getUsername()
        );
    }

}
