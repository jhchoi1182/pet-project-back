package com.springboot.petProject.util;

import com.springboot.petProject.exception.CustomExceptionHandler;
import com.springboot.petProject.exception.ErrorCode;
import com.springboot.petProject.repository.UserRepository;
import com.springboot.petProject.types.NameType;
import com.vane.badwordfiltering.BadWordFiltering;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Validate {
    private final UserRepository userRepository;
    private final BadWordFiltering badWordFiltering = new BadWordFiltering();

    public void validateName(String name, NameType nameType) {

        validateBlank(name);
        validateBadWord(name);

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

    private void validateBlank(String name) {
        if (name.isEmpty() || name.contains(" ")) {
            throw new CustomExceptionHandler(ErrorCode.INVALID_INFO, "Username cannot be empty or contain spaces");
        }
    }

    public void validateBadWord(String text) {
        boolean isBadWord = badWordFiltering.check(text);
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
