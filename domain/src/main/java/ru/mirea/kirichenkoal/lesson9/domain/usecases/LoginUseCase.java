package ru.mirea.kirichenkoal.lesson9.domain.usecases;

import ru.mirea.kirichenkoal.lesson9.domain.models.AuthResult;
import ru.mirea.kirichenkoal.lesson9.domain.repository.AuthRepository;

public class LoginUseCase {
    private final AuthRepository authRepository;

    public LoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(String email, String password, AuthRepository.AuthCallback callback) {
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

        // Делегируем репозиторию
        authRepository.loginWithEmail(email, password, callback);
    }
}