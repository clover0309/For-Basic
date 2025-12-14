package com.example.sbb.user;

import lombok.Getter;

@Getter
// 사용자의 역할 배분을 위한 UserRole을 enum으로 생성.
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
