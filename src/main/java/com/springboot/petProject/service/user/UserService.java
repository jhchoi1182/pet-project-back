package com.springboot.petProject.service.user;

import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.dto.service.AuthDto;
import com.springboot.petProject.entity.User;
import com.springboot.petProject.types.UserRole;
import com.springboot.petProject.exception.CustomExceptionHandler;
import com.springboot.petProject.exception.ErrorCode;
import com.springboot.petProject.repository.UserRepository;
import com.springboot.petProject.types.NameType;
import com.springboot.petProject.types.UserType;
import com.springboot.petProject.util.JwtTokenUtils;
import com.springboot.petProject.util.Validate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Validate validateName;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    @Transactional
    public UserDto createUser(String username, String nickname, String email, String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new CustomExceptionHandler(ErrorCode.PASSWORDS_NOT_MATCHING, "Passwords do not match");
        }

        validateName.validateName(username, NameType.USERNAME);
        validateName.validateName(nickname, NameType.NICKNAME);

        User user = userRepository.save(User.of(username, nickname, email, encoder.encode(password), UserRole.USER, UserType.LOCAL));
        return UserDto.fromEntity(user);
    }

    public void checkName(String name, NameType nameType) {
        validateName.validateName(name, nameType);
    }

    public AuthDto login(String username, String password) {
        validateUsernameAndPasswordNotNull(username, password);
        UserDto user = findUser(username);
        if (user.getRemovedAt() != null) {
            throw new CustomExceptionHandler(ErrorCode.USER_REMOVED);
        }

        if (!encoder.matches(password, user.getPassword())) {
            throw new CustomExceptionHandler(ErrorCode.PASSWORDS_NOT_MATCHING);
        }
        String token = JwtTokenUtils.generateToken(user.getUsername(), user.getNickname(), secretKey, expiredTimeMs);
        return new AuthDto(token, user.getNickname());
    }

    public UserDto findUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new CustomExceptionHandler(ErrorCode.USER_NOT_FOUND));
        return UserDto.fromEntity(user);
    }

    public AuthDto extractNicknameAndTokenFromAuthentication(Authentication authentication) {
        if (authentication == null) throw new CustomExceptionHandler(ErrorCode.INVALID_TOKEN, "No authentication information found.");;

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDto user) {
            String token = JwtTokenUtils.generateToken(user.getUsername(), user.getNickname(), secretKey, expiredTimeMs);
            return new AuthDto(token, user.getNickname());
        } else throw new CustomExceptionHandler(ErrorCode.INVALID_TOKEN, "No authentication information found.");
    }

    private void validateUsernameAndPasswordNotNull(String username, String password) {
        if (username == null || password == null) {
            throw new CustomExceptionHandler(ErrorCode.HTTP_MESSAGE_NOT_READABLE);
        }
    }

    @Transactional
    public void deleteUser(String username) {
        UserDto user = findUser(username);
        if (user.getRemovedAt() == null) {
            userRepository.deleteById(user.getUserId());
        } else {
            throw new CustomExceptionHandler(ErrorCode.USER_NOT_FOUND, "User already deleted");
        }
    }

}
