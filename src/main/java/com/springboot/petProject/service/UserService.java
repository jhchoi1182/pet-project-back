package com.springboot.petProject.service;

import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.entity.User;
import com.springboot.petProject.entity.UserRole;
import com.springboot.petProject.exception.ErrorCode;
import com.springboot.petProject.exception.CustomExceptionHandler;
import com.springboot.petProject.repository.UserRepository;
import com.springboot.petProject.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    @Transactional
    public UserDto createUser(String username, String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new CustomExceptionHandler(ErrorCode.PASSWORDS_NOT_MATCHING, "Passwords do not match");
        }

        validateUsername(username);

        User user = userRepository.save(User.of(username, encoder.encode(password), UserRole.USER));
        return UserDto.fromEntity(user);
    }

    public void validateUsername(String username) {
        if (username.isEmpty() || username.contains(" ")) {
            throw new CustomExceptionHandler(ErrorCode.INVALID_INFO, "Username cannot be empty or contain spaces");
        } else if (username.length() < 2) {
            throw new CustomExceptionHandler(ErrorCode.INVALID_INFO, "Username should have atleast 2 characters");
        }

        userRepository.findByUsername(username).ifPresent(it -> {
            throw new CustomExceptionHandler(ErrorCode.DUPLICATED_USER_NAME, String.format("%s exists", username));
        });
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new CustomExceptionHandler(ErrorCode.USER_NOT_FOUND));

        if (user.getRemovedAt() != null) {
            throw new CustomExceptionHandler(ErrorCode.USER_REMOVED);
        }

        if (!encoder.matches(password, user.getPassword())) {
            throw new CustomExceptionHandler(ErrorCode.PASSWORDS_NOT_MATCHING);
        }
        return JwtTokenUtils.generateToken(username, user.getPassword(), secretKey, expiredTimeMs);
    }

    public String loginAsGuest() {
        User guestUser = userRepository.findByUsername("guest")
                .orElseGet(() -> userRepository.save(User.of("guest", encoder.encode("321321"), UserRole.GUEST)));

        return JwtTokenUtils.generateToken(guestUser.getUsername(), guestUser.getPassword(), secretKey, expiredTimeMs);
    }

    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new CustomExceptionHandler(ErrorCode.USER_NOT_FOUND));
        if (user.getRemovedAt() == null) {
            userRepository.deleteById(user.getId());
        } else {
            throw new CustomExceptionHandler(ErrorCode.USER_NOT_FOUND, "User already deleted");
        }
    }

}
