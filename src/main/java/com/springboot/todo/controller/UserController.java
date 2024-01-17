package com.springboot.todo.controller;

import com.springboot.todo.dto.UserDto;
import com.springboot.todo.dto.request.UserLoginRequest;
import com.springboot.todo.dto.request.UserSignupRequest;
import com.springboot.todo.dto.response.UserLoginResponse;
import com.springboot.todo.dto.response.MessageResponse;
import com.springboot.todo.dto.response.Response;
import com.springboot.todo.dto.response.UserSignupResponse;
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
    public Response<UserSignupResponse> createUser(@RequestBody UserSignupRequest request) {
        UserDto user = userService.createUser(request.getUsername(), request.getPassword(), request.getPasswordConfirm());
        return Response.success(UserSignupResponse.from(user));
    }

    @GetMapping("/check-username")
    public Response<MessageResponse> checkUser(String username) {
        userService.checkUser(username);
        return Response.success(new MessageResponse("The ID is available."));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<UserLoginResponse>> login(@RequestParam(value = "type", required = true) String type, @RequestBody UserLoginRequest request) {
        String token;
        if ("guest".equals(type)) {
            token = userService.loginAsGuest();
            return ResponseEntity.ok(Response.success(new UserLoginResponse(token)));
        } else if ("user".equals(type)) {
            token = userService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(Response.success(new UserLoginResponse(token)));
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Integer id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
