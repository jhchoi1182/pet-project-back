package com.springboot.petProject.controller;

import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.dto.request.user.UserCheckRequest;
import com.springboot.petProject.dto.request.user.UserLoginRequest;
import com.springboot.petProject.dto.request.user.UserSignupRequest;
import com.springboot.petProject.dto.response.MessageResponse;
import com.springboot.petProject.dto.response.Response;
import com.springboot.petProject.dto.response.user.UserLoginResponse;
import com.springboot.petProject.dto.response.user.UserSignupResponse;
import com.springboot.petProject.service.user.UserService;
import com.springboot.petProject.types.NameType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Response<UserSignupResponse> createUser(@Valid @RequestBody UserSignupRequest request) {
        UserDto user = userService.createUser(request.getUsername(), request.getNickname(), request.getEmail(), request.getPassword(), request.getPasswordConfirm());
        return Response.success(UserSignupResponse.fromDto(user));
    }

    @PostMapping("/check-username")
    public Response<MessageResponse> checkUsername(@Valid @RequestBody UserCheckRequest request) {
        userService.checkName(request.getUsername(), NameType.USERNAME);
        return Response.success(new MessageResponse("The Username is available."));
    }

    @PostMapping("/check-nickname")
    public Response<MessageResponse> checkNickname(@Valid @RequestBody UserCheckRequest request) {
        userService.checkName(request.getNickname(), NameType.NICKNAME);
        return Response.success(new MessageResponse("The Nickname is available."));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<UserLoginResponse>> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(Response.success(new UserLoginResponse(token)));
    }

    @DeleteMapping("/delete")
    public Response<Void> deleteUser(Authentication authentication) {
        userService.deleteUser(authentication.getName());
        return Response.success();
    }


}
