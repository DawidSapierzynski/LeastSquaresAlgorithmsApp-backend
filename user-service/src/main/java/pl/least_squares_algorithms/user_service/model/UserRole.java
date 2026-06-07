package pl.least_squares_algorithms.user_service.model;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");

    private final String code;

    UserRole(String code) {
        this.code = code;
    }
}
