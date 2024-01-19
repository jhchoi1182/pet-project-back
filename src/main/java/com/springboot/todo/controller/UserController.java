package com.springboot.todo.controller;

import com.springboot.todo.dto.UserDto;
import com.springboot.todo.dto.request.UserCheckRequest;
import com.springboot.todo.dto.request.UserLoginRequest;
import com.springboot.todo.dto.request.UserSignupRequest;
import com.springboot.todo.dto.response.MessageResponse;
import com.springboot.todo.dto.response.Response;
import com.springboot.todo.dto.response.UserLoginResponse;
import com.springboot.todo.dto.response.UserSignupResponse;
import com.springboot.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Response<UserSignupResponse> createUser(@RequestBody UserSignupRequest request) {
        UserDto user = userService.createUser(request.getUsername(), request.getPassword(), request.getPasswordConfirm());
        return Response.success(UserSignupResponse.fromDto(user));
    }

    @PostMapping("/check-username")
    public Response<MessageResponse> checkUser(@RequestBody UserCheckRequest request) {
        log.info(String.valueOf(request));
        userService.checkUser(request.getUsername());
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
