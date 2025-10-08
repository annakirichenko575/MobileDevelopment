package ru.mirea.kirichenkoal.lesson9.domain.usecases;

import ru.mirea.kirichenkoal.lesson9.domain.repository.PlantRepository;

public class RemovePlantsFromFavorityByID {
    private final PlantRepository repo;

    public RemovePlantsFromFavorityByID(PlantRepository repo) {
        this.repo = repo;
    }

    public void execute(String plantId) {
        repo.removePlantsFromFavorityByID(plantId);
    }
}
