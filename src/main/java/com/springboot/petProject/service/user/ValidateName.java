package com.springboot.petProject.service.user;

import com.springboot.petProject.entity.User;
import com.springboot.petProject.exception.CustomExceptionHandler;
import com.springboot.petProject.exception.ErrorCode;
import com.springboot.petProject.repository.UserRepository;
import com.springboot.petProject.service.types.NameType;
import com.vane.badwordfiltering.BadWordFiltering;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateName {
    private final UserRepository userRepository;

    public void validateName(String name, NameType nameType) {
        BadWordFiltering badWordFiltering = new BadWordFiltering();
        boolean isBadWord = badWordFiltering.check(name);

        validateBlank(name);
        validateBadWord(isBadWord);

        switch (nameType) {
            case USERNAME:
                validateUsername(name);
                break;
            case NICKNAME:
                validateNickname(name);
                break;
            default:
                throw new CustomExceptionHandler(ErrorCode.SERVER_ERROR, "Invalid name type");
        }
    }

    public void assignUniqueNicknameIfAbsent(String username, User user) {
        if (user.getNickname() == null) {
            String newNickname = username;
            int count = 0;
            while (userRepository.findByNickname(username).isPresent()) {
                newNickname = username + count;
                count++;
            }
            user.setNickname(newNickname);
            userRepository.save(user);
        }
    }

    private void validateBlank(String name) {
        if (name.isEmpty() || name.contains(" ")) {
            throw new CustomExceptionHandler(ErrorCode.INVALID_INFO, "Username cannot be empty or contain spaces");
        }
    }

    private void validateBadWord(boolean isBadWord) {
        if (isBadWord) {
            throw new CustomExceptionHandler(ErrorCode.PROFANITY_INCLUDED, "Invalid name type");
        }
    }

    private void validateUsername(String username) {
        userRepository.findByUsername(username).ifPresent(it -> {
            throw new CustomExceptionHandler(ErrorCode.DUPLICATED_NAME, String.format("%s exists", username));
        });
    }

    private void validateNickname(String nickname) {
        userRepository.findByNickname(nickname).ifPresent(it -> {
            throw new CustomExceptionHandler(ErrorCode.DUPLICATED_NAME, String.format("%s exists", nickname));
        });
    }
}
