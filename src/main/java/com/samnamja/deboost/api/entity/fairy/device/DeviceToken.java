package com.samnamja.deboost.api.entity.fairy.device;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "FAIRY_DEVICE_TOKEN")
public class DeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_token_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "refresh_token",length = 500, nullable = false)
    private String refreshToken;

    @Builder
    public DeviceToken(Device device, String refreshToken) {
        this.device = device;
        this.refreshToken = refreshToken;
    }

    public void updateFairyRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
