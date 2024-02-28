package com.springboot.petProject.controller;

import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.dto.request.user.UserCheckRequest;
import com.springboot.petProject.dto.request.user.UserLoginRequest;
import com.springboot.petProject.dto.request.user.UserSignupRequest;
import com.springboot.petProject.dto.request.user.UserSocialLoginRequest;
import com.springboot.petProject.dto.response.MessageResponse;
import com.springboot.petProject.dto.response.Response;
import com.springboot.petProject.dto.response.user.UserInfoResponse;
import com.springboot.petProject.dto.response.user.UserLoginResponse;
import com.springboot.petProject.dto.response.user.UserSignupResponse;
import com.springboot.petProject.dto.service.AuthDto;
import com.springboot.petProject.service.user.CookieService;
import com.springboot.petProject.service.user.SocialLoginService;
import com.springboot.petProject.service.user.UserService;
import com.springboot.petProject.types.NameType;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final SocialLoginService socialLoginService;
    private final CookieService cookieService;

    @PostMapping("/signup")
    public Response<UserSignupResponse> createUser(@Valid @RequestBody UserSignupRequest request) {
        UserDto user = userService.createUser(request.getUsername(), request.getNickname(), request.getEmail(), request.getPassword(), request.getPasswordConfirm());
        return Response.success(UserSignupResponse.fromDto(user));
    }

    @GetMapping("")
    public Response<UserInfoResponse> getUserInfo(Authentication authentication, HttpServletResponse response) {
        AuthDto authDto = cookieService.extractAuthenticationAndSetHeaderCookie(authentication, response , false);
        return Response.success(new UserInfoResponse(authDto.getNickname()));
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
    public ResponseEntity<Response<UserLoginResponse>> login(@RequestBody UserLoginRequest request, HttpServletResponse response) {
        AuthDto authDto = userService.login(request.getUsername(), request.getPassword());
        cookieService.setHeaderAuthenticationCookie(response, authDto.getToken(), false);

        return ResponseEntity.ok()
                .body(Response.success(new UserLoginResponse(authDto.getNickname())));
    }

    @PostMapping("/google")
    public ResponseEntity<Response<UserLoginResponse>> googleLogin(@RequestBody UserSocialLoginRequest request, HttpServletResponse response) {
        AuthDto authDto = socialLoginService.authenticateGoogleLogin(request.getCode());
        cookieService.setHeaderAuthenticationCookie(response, authDto.getToken(), false);
        return ResponseEntity.ok()
                .body(Response.success(new UserLoginResponse(authDto.getNickname())));
    }

    @PostMapping("/logout")
    public Response<Void> returnExpiredToken(Authentication authentication, HttpServletResponse response) {
        log.info("되냐???");
        cookieService.extractAuthenticationAndSetHeaderCookie(authentication, response , true);
        return Response.success();
    }

    @DeleteMapping("/delete")
    public Response<Void> deleteUser(Authentication authentication, HttpServletResponse response) {
        cookieService.extractAuthenticationAndSetHeaderCookie(authentication, response , true);
        return Response.success();
    }

}
