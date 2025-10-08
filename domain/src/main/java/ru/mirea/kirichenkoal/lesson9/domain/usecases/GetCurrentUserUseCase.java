package ru.mirea.kirichenkoal.lesson9.domain.usecases;

import ru.mirea.kirichenkoal.lesson9.domain.models.User;
import ru.mirea.kirichenkoal.lesson9.domain.repository.AuthRepository;

public class GetCurrentUserUseCase {
    private final AuthRepository authRepository;

    public GetCurrentUserUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public User execute() {
        return authRepository.getCurrentUser();
    }

    public boolean isUserLoggedIn() {
        return authRepository.isUserLoggedIn();
    }
}