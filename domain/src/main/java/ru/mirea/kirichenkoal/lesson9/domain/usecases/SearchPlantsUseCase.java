package ru.mirea.kirichenkoal.lesson9.domain.usecases;

import ru.mirea.kirichenkoal.lesson9.domain.repository.PlantRepository;

public class SearchPlantsUseCase {

    private final PlantRepository plantRepository;

    public SearchPlantsUseCase(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public void execute(String query, PlantRepository.PlantSearchCallback callback) {
        if (query == null || query.trim().isEmpty()) {
            callback.onError("Поисковый запрос не может быть пустым");
            return;
        }

        if (query.length() < 2) {
            callback.onError("Введите минимум 2 символа для поиска");
            return;
        }

        plantRepository.searchPlants(query.trim(), callback);
    }
}