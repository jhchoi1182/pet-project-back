package com.springboot.petProject.dto.response;

import com.springboot.petProject.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserSignupResponse {

    private Integer userId;
    private String username;
    private String nickname;

    public static UserSignupResponse fromDto(UserDto user) {
        return new UserSignupResponse(
                user.getUserId(),
                user.getUsername(),
                user.getNickname()
        );
    }

}
