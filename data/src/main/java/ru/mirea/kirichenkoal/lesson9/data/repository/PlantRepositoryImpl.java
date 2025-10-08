package ru.mirea.kirichenkoal.lesson9.data.repository;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ru.mirea.kirichenkoal.lesson9.data.database.AppDatabase;
import ru.mirea.kirichenkoal.lesson9.data.database.dao.PlantDao;
import ru.mirea.kirichenkoal.lesson9.data.database.entity.PlantEntity;
import ru.mirea.kirichenkoal.lesson9.data.network.PlantNetworkApi;
import ru.mirea.kirichenkoal.lesson9.data.network.models.ApiPlant;
import ru.mirea.kirichenkoal.lesson9.data.storage.PlantStorage;
import ru.mirea.kirichenkoal.lesson9.data.storage.models.StoragePlant;
import ru.mirea.kirichenkoal.lesson9.data.storage.sharedprefs.SharedPrefPlantStorage;
import ru.mirea.kirichenkoal.lesson9.domain.models.Plant;
import ru.mirea.kirichenkoal.lesson9.domain.repository.PlantRepository;

public class PlantRepositoryImpl implements PlantRepository {

    private final PlantStorage plantStorage;
    private final PlantDao plantDao;
    private final PlantNetworkApi plantNetworkApi;
    private final Executor executor;
    private final List<Plant> demoPlants = new ArrayList<>();
    private final Context context;

    public PlantRepositoryImpl(Context context) {
        this.context = context;
        this.plantStorage = new SharedPrefPlantStorage(context);
        this.plantDao = AppDatabase.getInstance(context).plantDao();
        this.plantNetworkApi = new PlantNetworkApi();
        this.executor = Executors.newFixedThreadPool(2);
        initDemoPlants();
    }

    private void initDemoPlants() {
        demoPlants.add(new Plant(1, "Фикус"));
        demoPlants.add(new Plant(2, "Алоэ"));
        demoPlants.add(new Plant(3, "Монстера"));
        demoPlants.add(new Plant(4, "Кактус"));
        demoPlants.add(new Plant(5, "Орхидея"));
    }

    @Override
    public boolean savePlant(Plant plant) {
        // 1. Сохраняем в SharedPreferences (основное любимое растение)
        StoragePlant storagePlant = mapToStorage(plant);
        boolean sharedPrefsResult = plantStorage.saveFavoritePlant(storagePlant);

        // 2. Сохраняем в Room Database (для истории и расширенной информации)
        executor.execute(() -> {
            PlantEntity plantEntity = new PlantEntity(
                    plant.getName(),
                    "Комнатное растение", // тип можно определить логикой
                    "Добавлено пользователем",
                    System.currentTimeMillis(),
                    true
            );
            plantDao.insertPlant(plantEntity);
        });

        return sharedPrefsResult;
    }

    @Override
    public Plant getFavoritePlant() {
        // Получаем из SharedPreferences
        StoragePlant storagePlant = plantStorage.getFavoritePlant();
        return mapToDomain(storagePlant);
    }

    @Override
    public void addPlantsToFavorityByID(String plantId) {
        // Сохраняем ID в SharedPreferences (список избранных)
        plantStorage.addToFavorites(plantId);

        // Также обновляем в Room Database
        executor.execute(() -> {
            try {
                int id = Integer.parseInt(plantId);
                PlantEntity plant = plantDao.getPlantById(id);
                if (plant != null) {
                    plant.isFavorite = true;
                    plantDao.updatePlant(plant);
                }
            } catch (NumberFormatException e) {
                // Обработка ошибки парсинга
            }
        });
    }

    @Override
    public void removePlantsFromFavorityByID(String plantId) {
        // Удаляем из SharedPreferences
        plantStorage.removeFromFavorites(plantId);

        // Обновляем в Room Database
        executor.execute(() -> {
            try {
                int id = Integer.parseInt(plantId);
                PlantEntity plant = plantDao.getPlantById(id);
                if (plant != null) {
                    plant.isFavorite = false;
                    plantDao.updatePlant(plant);
                }
            } catch (NumberFormatException e) {
                // Обработка ошибки парсинга
            }
        });
    }

    @Override
    public List<Plant> getFavorityPlantByPage(int limit, int offset) {
        List<PlantEntity> entities = plantDao.getFavoritePlants(); // все избранные
        List<Plant> result = new ArrayList<>();

        int start = Math.min(offset, entities.size());
        int end = Math.min(offset + limit, entities.size());

        for (int i = start; i < end; i++) {
            PlantEntity e = entities.get(i);
            result.add(new Plant(e.id, e.name));
        }

        return result;
    }

    /**
     * Поиск растений через NetworkApi
     */
    @Override
    public void searchPlants(String query, PlantSearchCallback callback) {
        executor.execute(() -> {
            try {
                List<ApiPlant> apiPlants = plantNetworkApi.searchPlants(query);
                List<Plant> plants = new ArrayList<>();

                for (ApiPlant apiPlant : apiPlants) {
                    plants.add(new Plant(apiPlant.id, apiPlant.name));
                }

                // Сохраняем найденные растения в Room для кэширования
                for (ApiPlant apiPlant : apiPlants) {
                    PlantEntity entity = new PlantEntity(
                            apiPlant.name,
                            apiPlant.family,
                            apiPlant.description,
                            System.currentTimeMillis(),
                            false
                    );
                    plantDao.insertPlant(entity);
                }

                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> callback.onSuccess(plants));
                } else {
                    callback.onSuccess(plants);
                }
            } catch (Exception e) {
                // ВЫЗЫВАЕМ В UI ПОТОКЕ
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> callback.onError(e.getMessage()));
                } else {
                    callback.onError(e.getMessage());
                }
            }
        });
    }

    /**
     * Получение растений из Room Database
     */
    @Override
    public void getPlantsFromDatabase(PlantDatabaseCallback callback) {
        executor.execute(() -> {
            try {
                List<PlantEntity> entities = plantDao.getRecentPlants(10);
                List<Plant> plants = new ArrayList<>();

                for (PlantEntity entity : entities) {
                    plants.add(new Plant(entity.id, entity.name));
                }

                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> callback.onSuccess(plants));
                } else {
                    callback.onSuccess(plants);
                }
            } catch (Exception e) {
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> callback.onError(e.getMessage()));
                } else {
                    callback.onError(e.getMessage());
                }
            }
        });
    }

    /**
     * Получение избранных растений из Room Database
     */
    @Override
    public void getFavoritePlantsFromDatabase(PlantDatabaseCallback callback) {
        executor.execute(() -> {
            try {
                List<PlantEntity> entities = plantDao.getFavoritePlants();
                List<Plant> plants = new ArrayList<>();

                for (PlantEntity entity : entities) {
                    plants.add(new Plant(entity.id, entity.name));
                }

                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> callback.onSuccess(plants));
                } else {
                    callback.onSuccess(plants);
                }
            } catch (Exception e) {
                // ВЫЗЫВАЕМ В UI ПОТОКЕ
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> callback.onError(e.getMessage()));
                } else {
                    callback.onError(e.getMessage());
                }
            }
        });
    }

    private Plant findDemoPlantById(int id) {
        for (Plant plant : demoPlants) {
            if (plant.getId() == id) {
                return plant;
            }
        }
        return null;
    }

    // Методы для маппинга между моделями
    private StoragePlant mapToStorage(Plant plant) {
        return new StoragePlant(plant.getId(), plant.getName(), "");
    }

    private Plant mapToDomain(StoragePlant storagePlant) {
        return new Plant(storagePlant.getId(), storagePlant.getName());
    }
}