package pl.leastsquaresalgorithms.userservice.model;

public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");

    private final String code;

    UserRole(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
