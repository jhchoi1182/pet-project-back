package com.springboot.petProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignupRequest {

    private String username;
    private String password;
    private String passwordConfirm;

}
