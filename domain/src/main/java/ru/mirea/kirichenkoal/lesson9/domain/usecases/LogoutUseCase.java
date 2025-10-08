package ru.mirea.kirichenkoal.lesson9.domain.usecases;

import ru.mirea.kirichenkoal.lesson9.domain.repository.AuthRepository;

public class LogoutUseCase {
    private final AuthRepository authRepository;

    public LogoutUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute() {
        authRepository.logout();
    }
}