package com.samnamja.deboost.api.service.fairy.auth;

import com.samnamja.deboost.api.dto.auth.TokenResponseDto;
import com.samnamja.deboost.api.dto.fairy.request.FairyAuthRequestDto;
import com.samnamja.deboost.api.entity.fairy.device.Device;
import com.samnamja.deboost.api.entity.fairy.device.DeviceRepository;
import com.samnamja.deboost.api.entity.fairy.device.DeviceToken;
import com.samnamja.deboost.api.entity.fairy.device.DeviceTokenRepository;
import com.samnamja.deboost.api.entity.user.Role;
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
public class FairyAuthService {
    private final DeviceRepository deviceRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenResponseDto issuingFairyTokens(FairyAuthRequestDto fairyAuthRequestDto){
        Optional<Device> device = deviceRepository.findByDeviceName(fairyAuthRequestDto.getDeviceName());
        if (device.isPresent()){ // 있으면 token 발행
            String accessToken = jwtTokenUtil.generateAccessToken(device.get().getId(), device.get().getDeviceName(), device.get().getRole(), true);
            String refreshToken = jwtTokenUtil.generateRefreshToken(device.get().getId(), device.get().getDeviceName(), device.get().getRole(), true);

            Optional<DeviceToken> token = deviceTokenRepository.findByDevice_Id(device.get().getId());
            if (token.isPresent()){
                token.get().updateFairyRefreshToken(refreshToken);
            } else {
                deviceTokenRepository.save(DeviceToken.builder()
                        .device(device.get())
                        .refreshToken(refreshToken).build());
            }

            return TokenResponseDto.of(accessToken, refreshToken);
        } else { // 없으면 회원가입 후 TOKEN 발행
            Device newDevice = deviceRepository.save(Device.builder()
                    .deviceName(fairyAuthRequestDto.getDeviceName())
                    .role(Role.ROLE_USER).build());

            String accessToken = jwtTokenUtil.generateAccessToken(newDevice.getId(), newDevice.getDeviceName(), newDevice.getRole(), true);
            String refreshToken = jwtTokenUtil.generateRefreshToken(newDevice.getId(), newDevice.getDeviceName(), newDevice.getRole(), true);

            deviceTokenRepository.save(DeviceToken.builder()
                    .device(newDevice)
                    .refreshToken(refreshToken).build());

            return TokenResponseDto.of(accessToken, refreshToken);
        }
    }
}
