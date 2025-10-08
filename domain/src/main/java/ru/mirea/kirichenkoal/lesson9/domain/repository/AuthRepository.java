package ru.mirea.kirichenkoal.lesson9.domain.repository;

import ru.mirea.kirichenkoal.lesson9.domain.models.AuthResult;
import ru.mirea.kirichenkoal.lesson9.domain.models.User;

public interface AuthRepository {
    // Методы авторизации
    void loginWithEmail(String email, String password, AuthCallback callback);
    void registerWithEmail(String email, String password, String displayName, AuthCallback callback);
    void logout();

    // Проверка состояния авторизации
    boolean isUserLoggedIn();
    User getCurrentUser();

    // Callback интерфейс для асинхронных операций
    interface AuthCallback {
        void onComplete(AuthResult result);
    }
}