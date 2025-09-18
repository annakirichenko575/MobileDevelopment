package ru.mirea.kirichenkoal.lesson9.domain.repository;

import java.util.List;
import ru.mirea.kirichenkoal.lesson9.domain.models.Plant;

public interface PlantRepository {
    boolean savePlant(Plant plant);
    Plant getFavoritePlant();

    // для экрана "Избранное":
    void addPlantsToFavorityByID(String plantId);
    void removePlantsFromFavorityByID(String plantId);
    List<Plant> getFavorityPlantByPage(int limit, int offset);
}
