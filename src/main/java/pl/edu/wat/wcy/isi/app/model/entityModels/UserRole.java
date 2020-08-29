package pl.edu.wat.wcy.isi.app.model.entityModels;

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
