package ru.mirea.kirichenkoal.lesson9.domain.models;

public class AuthResult {
    private final boolean success;
    private final String message;
    private final User user;

    public AuthResult(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    // Геттеры
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public User getUser() { return user; }

    // Вспомогательные методы
    public static AuthResult success(User user) {
        return new AuthResult(true, "Success", user);
    }

    public static AuthResult error(String message) {
        return new AuthResult(false, message, null);
    }
}