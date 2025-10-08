package ru.mirea.kirichenkoal.lesson9.domain.models;

public class User {
    private String uid;
    private String email;
    private String displayName;
    private boolean isEmailVerified;

    public User(String uid, String email, String displayName, boolean isEmailVerified) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.isEmailVerified = isEmailVerified;
    }

    // Геттеры
    public String getUid() { return uid; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public boolean isEmailVerified() { return isEmailVerified; }

    // Пустой конструктор для Firebase
    public User() {}
}