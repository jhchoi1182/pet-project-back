package com.springboot.petProject.dto.request.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignupRequest {

    @Size(min = 2, max = 16, message = "ID should have between 2 and 16 characters")
    private String username;
    @Size(min = 2, max = 16, message = "ID should have between 2 and 16 characters")
    private String nickname;
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Invalid email format")
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,20}$",
            message = "Password must contain at least one lowercase letter, one digit, and one special character, and must be 8-20 characters")
    private String password;
    private String passwordConfirm;

}
