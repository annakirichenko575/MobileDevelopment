package ru.mirea.kirichenkoal.lesson9.domain.usecases;

import ru.mirea.kirichenkoal.lesson9.domain.models.AuthResult;
import ru.mirea.kirichenkoal.lesson9.domain.repository.AuthRepository;

public class RegisterUseCase {
    private final AuthRepository authRepository;

    public RegisterUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(String email, String password, String displayName, AuthRepository.AuthCallback callback) {
        // Валидация email
        if (email == null || email.isEmpty()) {
            callback.onComplete(AuthResult.error("Email cannot be empty"));
            return;
        }

        // Валидация пароля
        if (password == null || password.isEmpty()) {
            callback.onComplete(AuthResult.error("Password cannot be empty"));
            return;
        }

        // Валидация имени
        if (displayName == null || displayName.isEmpty()) {
            callback.onComplete(AuthResult.error("Display name cannot be empty"));
            return;
        }

        // Проверка длины пароля
        if (password.length() < 6) {
            callback.onComplete(AuthResult.error("Password must be at least 6 characters"));
            return;
        }

        // Делегируем репозиторию
        authRepository.registerWithEmail(email, password, displayName, callback);
    }
}