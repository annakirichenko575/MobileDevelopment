package ru.mirea.kirichenkoal.lesson9.domain.repository;

import java.util.List;
import ru.mirea.kirichenkoal.lesson9.domain.models.Plant;

public interface PlantRepository {
    // Существующие методы
    boolean savePlant(Plant plant);
    Plant getFavoritePlant();
    void addPlantsToFavorityByID(String plantId);
    void removePlantsFromFavorityByID(String plantId);
    List<Plant> getFavorityPlantByPage(int limit, int offset);

    // Новые методы для работы с разными источниками данных
    void searchPlants(String query, PlantSearchCallback callback);
    void getPlantsFromDatabase(PlantDatabaseCallback callback);
    void getFavoritePlantsFromDatabase(PlantDatabaseCallback callback);

    // Callback интерфейсы
    interface PlantSearchCallback {
        void onSuccess(List<Plant> plants);
        void onError(String message);
    }

    interface PlantDatabaseCallback {
        void onSuccess(List<Plant> plants);
        void onError(String message);
    }
}