package com.springboot.todo.service;

import com.springboot.todo.dto.UserDto;
import com.springboot.todo.entity.User;
import com.springboot.todo.entity.UserRole;
import com.springboot.todo.exception.ErrorCode;
import com.springboot.todo.exception.TodoExceptionHandler;
import com.springboot.todo.repository.UserRepository;
import com.springboot.todo.util.JwtTokenUtils;
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
            throw new TodoExceptionHandler(ErrorCode.PASSWORDS_NOT_MATCHING, "Passwords do not match");
        }

        userRepository.findByUsername(username).ifPresent(it -> {
                    throw new TodoExceptionHandler(ErrorCode.DUPLICATED_USER_NAME, String.format("%s exists", username));
        });

        User user = userRepository.save(User.of(username, encoder.encode(password), UserRole.USER));
        return UserDto.from(user);
    }

    public void checkUser(String username) {
        if (username == null || username.contains(" ")) {
            throw new TodoExceptionHandler(ErrorCode.INVALID_INFO, "Username cannot be empty or contain spaces");
        }
        userRepository.findByUsername(username).ifPresent(it -> {
            throw new TodoExceptionHandler(ErrorCode.DUPLICATED_USER_NAME, String.format("%s exists", username));
        });
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new TodoExceptionHandler(ErrorCode.USER_NOT_FOUND));

        if (!encoder.matches(password, user.getPassword())) {
            throw new TodoExceptionHandler(ErrorCode.INVALID_INFO);
        }
        return JwtTokenUtils.generateToken(username, user.getPassword(), secretKey, expiredTimeMs);
    }

    public String loginAsGuest() {
        User guestUser = userRepository.findByUsername("guest")
                .orElseGet(() -> userRepository.save(User.of("guest", encoder.encode("321321"), UserRole.GUEST)));

        return JwtTokenUtils.generateToken(guestUser.getUsername(), guestUser.getPassword(), secretKey, expiredTimeMs);
    }

    @Transactional
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new TodoExceptionHandler(ErrorCode.USER_NOT_FOUND));
        if (user.getRemovedAt() == null) {
            userRepository.deleteById(id);
        } else {
            throw new TodoExceptionHandler(ErrorCode.USER_NOT_FOUND, "User already deleted");
        }
    }

}
