package ru.mirea.kirichenkoal.lesson9.domain.usecases;

import java.util.List;
import ru.mirea.kirichenkoal.lesson9.domain.models.Plant;
import ru.mirea.kirichenkoal.lesson9.domain.repository.PlantRepository;

public class GetFavorityPlantByPage {
    private final PlantRepository repo;

    public GetFavorityPlantByPage(PlantRepository repo) {
        this.repo = repo;
    }

    public List<Plant> execute(int limit, int offset) {
        return repo.getFavorityPlantByPage(limit, offset);
    }
}
