package ru.mirea.kirichenkoal.lesson9.domain.usecases;

import ru.mirea.kirichenkoal.lesson9.domain.repository.PlantRepository;

public class GetPlantsFromDatabaseUseCase {

    private final PlantRepository plantRepository;

    public GetPlantsFromDatabaseUseCase(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public void execute(PlantRepository.PlantDatabaseCallback callback) {
        plantRepository.getPlantsFromDatabase(callback);
    }
}