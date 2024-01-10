package com.springboot.todo.controller;

import com.springboot.todo.dto.UserDto;
import com.springboot.todo.dto.request.LoginRequest;
import com.springboot.todo.dto.request.SignupRequest;
import com.springboot.todo.dto.response.LoginResponse;
import com.springboot.todo.dto.response.Response;
import com.springboot.todo.dto.response.SignupResponse;
import com.springboot.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Response<SignupResponse> createUser(@RequestBody SignupRequest request) {
        UserDto user = userService.createUser(request.getUsername(), request.getPassword(), request.getPasswordConfirm());
        return Response.success(SignupResponse.from(user));
    }

    @PostMapping("/login")
    public Response<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = userService.login(request.getUsername(), request.getPassword());
        return Response.success(new LoginResponse(token));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
