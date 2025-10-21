package ru.mirea.kirichenkoal.lesson9.presentation.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.kirichenkoal.lesson9.domain.repository.PlantRepository;

/**
 * Фабрика для передачи репозитория в ViewModel.
 */
public class PlantViewModelFactory implements ViewModelProvider.Factory {
    private final PlantRepository repository;

    public PlantViewModelFactory(PlantRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PlantViewModel.class)) {
            return (T) new PlantViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
