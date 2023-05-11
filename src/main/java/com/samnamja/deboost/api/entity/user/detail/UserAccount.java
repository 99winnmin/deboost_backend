package com.samnamja.deboost.api.entity.user.detail;

import com.samnamja.deboost.api.entity.user.Role;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class UserAccount extends org.springframework.security.core.userdetails.User {

    private Long userId;

    private String email;

    private Role role;

    public UserAccount(Long userId, String email, Role role) {
        super(email, email, new ArrayList<Role>() {{
            add(role);
        }});
        this.userId = userId;
        this.email = email;
        this.role = role;
    }
}
