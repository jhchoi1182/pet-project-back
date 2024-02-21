package com.springboot.petProject.service.user;

import com.springboot.petProject.dto.UserDto;
import com.springboot.petProject.entity.User;
import com.springboot.petProject.entity.UserRole;
import com.springboot.petProject.exception.CustomExceptionHandler;
import com.springboot.petProject.exception.ErrorCode;
import com.springboot.petProject.repository.UserRepository;
import com.springboot.petProject.service.types.NameType;
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
    private final ValidateName validateName;
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

        User user = userRepository.save(User.of(username, nickname, email, encoder.encode(password), UserRole.USER));
        return UserDto.fromEntity(user);
    }

    public void checkName(String name, NameType nameType) {
        validateName.validateName(name, nameType);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new CustomExceptionHandler(ErrorCode.USER_NOT_FOUND));

        if (user.getRemovedAt() != null) {
            throw new CustomExceptionHandler(ErrorCode.USER_REMOVED);
        }

        validateName.assignUniqueNicknameIfAbsent(username, user);

        if (!encoder.matches(password, user.getPassword())) {
            throw new CustomExceptionHandler(ErrorCode.PASSWORDS_NOT_MATCHING);
        }
        return JwtTokenUtils.generateToken(username, user.getPassword(), secretKey, expiredTimeMs);
    }

    public String loginAsGuest() {
        User guestUser = userRepository.findByUsername("guest")
                .orElseGet(() -> userRepository.save(User.of("guest", "게스트", "test@test.com", encoder.encode("321321"), UserRole.GUEST)));

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
