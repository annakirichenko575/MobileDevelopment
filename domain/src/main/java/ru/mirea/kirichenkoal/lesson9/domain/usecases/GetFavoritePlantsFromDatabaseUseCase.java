package ru.mirea.kirichenkoal.lesson9.domain.usecases;

import ru.mirea.kirichenkoal.lesson9.domain.repository.PlantRepository;

public class GetFavoritePlantsFromDatabaseUseCase {

    private final PlantRepository plantRepository;

    public GetFavoritePlantsFromDatabaseUseCase(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public void execute(PlantRepository.PlantDatabaseCallback callback) {
        plantRepository.getFavoritePlantsFromDatabase(callback);
    }
}