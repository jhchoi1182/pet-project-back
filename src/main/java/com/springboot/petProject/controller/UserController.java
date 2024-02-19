package com.springboot.petProject.controller;

import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.dto.request.UserCheckRequest;
import com.springboot.petProject.dto.request.UserLoginRequest;
import com.springboot.petProject.dto.request.UserSignupRequest;
import com.springboot.petProject.dto.response.MessageResponse;
import com.springboot.petProject.dto.response.Response;
import com.springboot.petProject.dto.response.UserLoginResponse;
import com.springboot.petProject.dto.response.UserSignupResponse;
import com.springboot.petProject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Response<UserSignupResponse> createUser(@RequestBody UserSignupRequest request) {
        UserDto user = userService.createUser(request.getUsername(), request.getPassword(), request.getPasswordConfirm());
        return Response.success(UserSignupResponse.fromDto(user));
    }

    @PostMapping("/check-username")
    public Response<MessageResponse> checkUser(@RequestBody UserCheckRequest request) {
        userService.validateUsername(request.getUsername());
        return Response.success(new MessageResponse("The ID is available."));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<UserLoginResponse>> login(@RequestParam(value = "type", required = true) String type, @RequestBody(required = false) UserLoginRequest request) {
        String token;
        if ("guest".equals(type)) {
            token = userService.loginAsGuest();
            return ResponseEntity.ok(Response.success(new UserLoginResponse(token)));
        } else if ("user".equals(type)) {
            if (request == null || request.getUsername() == null || request.getPassword() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            token = userService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(Response.success(new UserLoginResponse(token)));
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete")
    public Response<Void> deleteUser(Authentication authentication) {
        userService.deleteUser(authentication.getName());
        return Response.success();
    }


}
