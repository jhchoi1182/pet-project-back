package com.springboot.petProject.service.user;

import com.springboot.petProject.dto.response.user.UserLoginResponse;
import com.springboot.petProject.entity.User;
import com.springboot.petProject.exception.CustomExceptionHandler;
import com.springboot.petProject.exception.ErrorCode;
import com.springboot.petProject.repository.UserRepository;
import com.springboot.petProject.types.UserRole;
import com.springboot.petProject.types.UserType;
import com.springboot.petProject.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;
    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String googleClientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirectUri}")
    private String googleRedirectUri;
    @Value("${spring.security.oauth2.client.provider.google.tokenUri}")
    private String googleTokenUri;
    @Value("${spring.security.oauth2.client.provider.google.userInfoUri}")
    private String googleUserInfoUri;

    public UserLoginResponse authenticateGoogleLogin(String code) {
        String accessToken = getGoogleToken(code);
        Map<String, Object> userDetails = getGoogleUserInfo(accessToken);

        String email = (String) userDetails.get("email");
        String username = "GOOGLE_" + email.split("@")[0];
        String nickname = "GOOGLE_" + email.split("@")[0];
        User user = ensureUniqueGoogleUser(email, username, nickname);
        String token = JwtTokenUtils.generateToken(user.getUsername(), encoder.encode(user.getUsername()), secretKey, expiredTimeMs);

        return new UserLoginResponse(token, user.getNickname());
    }

    private User ensureUniqueGoogleUser(String email, String initialUsername, String initialNickname) {
        User user = userRepository.findByEmail(email).orElse(null);

        log.info("디비에 있는 비밀번호 {}", user.getPassword());
        String username = initialUsername;
        String nickname = initialNickname;

        if (user == null || !user.getType().equals(UserType.GOOGLE)) {
            boolean isUnique = false;
            int attempt = 0;
            while (!isUnique) {
                if (userRepository.findByUsername(username).isPresent() || userRepository.findByNickname(nickname).isPresent()) {
                    String uniqueSuffix = "_" + new Random().nextInt(1000);
                    nickname = initialNickname + uniqueSuffix;
                    username = initialUsername + uniqueSuffix;
                    attempt++;
                    if (attempt > 10) {
                        throw new CustomExceptionHandler(ErrorCode.DUPLICATED_NAME, "Failed to generate a unique username/nickname");
                    }
                } else {
                    isUnique = true;
                }
            }
            user = userRepository.save(User.of(username, nickname, email, encoder.encode(username), UserRole.USER, UserType.GOOGLE));
        }
        return user;
    }

    private String getGoogleToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("code", code);
        params.add("redirect_uri", googleRedirectUri);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(googleTokenUri, HttpMethod.POST, requestEntity, Map.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = responseEntity.getBody();
            return (String) responseBody.get("access_token");
        } else {
            throw new CustomExceptionHandler(ErrorCode.SERVER_ERROR, "Failed to exchange token with Google");
        }
    }

    private Map<String, Object> getGoogleUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(googleUserInfoUri, HttpMethod.GET, entity, Map.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new CustomExceptionHandler(ErrorCode.SERVER_ERROR, "Failed to fetch user info from Google");
        }
    }

}
