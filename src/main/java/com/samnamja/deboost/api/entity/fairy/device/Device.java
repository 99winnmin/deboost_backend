package com.samnamja.deboost.api.entity.fairy.device;

import com.samnamja.deboost.api.entity.fairy.diary.Diary;
import com.samnamja.deboost.api.entity.user.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "FAIRY_DEVICE")
public class Device {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private Long id;

    @Column(name = "device_name", nullable = false)
    private String deviceName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "device_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "device", cascade = CascadeType.ALL)
    private DeviceToken deviceToken;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<Diary> diaries;

    @Builder
    public Device(String deviceName, Role role) {
        this.deviceName = deviceName;
        this.role = role;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }
}
