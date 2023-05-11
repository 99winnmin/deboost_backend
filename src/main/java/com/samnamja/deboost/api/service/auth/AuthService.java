package com.samnamja.deboost.api.service.auth;

import com.samnamja.deboost.api.dto.auth.GoogleUserInfoDto;
import com.samnamja.deboost.api.dto.auth.TokenResponseDto;
import com.samnamja.deboost.api.entity.token.UserToken;
import com.samnamja.deboost.api.entity.token.UserTokenRepository;
import com.samnamja.deboost.api.entity.user.Role;
import com.samnamja.deboost.api.entity.user.User;
import com.samnamja.deboost.api.entity.user.UserRepository;
import com.samnamja.deboost.filter.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenResponseDto issuingTokens(GoogleUserInfoDto googleUserInfoDto){
        Optional<User> user = userRepository.findUserByEmail(googleUserInfoDto.getEmail());
        if (user.isPresent()){ // 있으면 token 발행
            String accessToken = jwtTokenUtil.generateAccessToken(user.get().getId(), user.get().getEmail(), user.get().getRole(), true);
            String refreshToken = jwtTokenUtil.generateRefreshToken(user.get().getId(), user.get().getEmail(), user.get().getRole(), true);

            // TODO: refreshToken update 로직 추가해야함
            Optional<UserToken> token = userTokenRepository.findUserTokenById(user.get().getId());
            if (token.isPresent()){
                token.get().updateRefreshToken(refreshToken);
            } else {
                userTokenRepository.save(UserToken.builder()
                                .user(user.get())
                                .refreshToken(refreshToken)
                                .build());
            }

            return TokenResponseDto.of(accessToken, refreshToken);
        } else { // 없으면 회원가입 후 TOKEN 발행
            User newUser = userRepository.save(User.builder()
                    .email(googleUserInfoDto.getEmail())
                    .name(googleUserInfoDto.getName())
                    .picture(googleUserInfoDto.getPicture())
                    .role(Role.ROLE_USER)
                    .build());

            String accessToken = jwtTokenUtil.generateAccessToken(newUser.getId(), newUser.getEmail(), newUser.getRole(), true);
            String refreshToken = jwtTokenUtil.generateRefreshToken(newUser.getId(), newUser.getEmail(), newUser.getRole(), true);

            userTokenRepository.save(UserToken.builder()
                            .user(newUser)
                            .refreshToken(refreshToken)
                            .build());

            return TokenResponseDto.of(accessToken, refreshToken);
        }
    }
}
