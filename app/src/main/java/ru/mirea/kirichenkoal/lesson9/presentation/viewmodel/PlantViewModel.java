package ru.mirea.kirichenkoal.lesson9.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.kirichenkoal.lesson9.domain.models.Plant;
import ru.mirea.kirichenkoal.lesson9.domain.repository.PlantRepository;

/**
 * ViewModel для работы с растениями.
 * Использует MediatorLiveData для объединения данных из локальной БД и mock-сети.
 */
public class PlantViewModel extends ViewModel {

    private final PlantRepository repository;

    // Данные из локальной БД
    private final MutableLiveData<List<Plant>> plantsFromDb = new MutableLiveData<>();

    // Данные из mock-сети
    private final MutableLiveData<List<Plant>> plantsFromNetwork = new MutableLiveData<>();

    // Итоговое объединённое значение
    private final MediatorLiveData<List<Plant>> combinedPlants = new MediatorLiveData<>();

    public PlantViewModel(PlantRepository repository) {
        this.repository = repository;

        // Объединяем данные из БД и сети
        combinedPlants.addSource(plantsFromDb, dbData -> {
            List<Plant> current = combinedPlants.getValue() != null
                    ? new ArrayList<>(combinedPlants.getValue())
                    : new ArrayList<>();

            if (dbData != null) {
                current.clear();
                current.addAll(dbData);
                combinedPlants.setValue(current);
            }
        });

        combinedPlants.addSource(plantsFromNetwork, netData -> {
            List<Plant> current = combinedPlants.getValue() != null
                    ? new ArrayList<>(combinedPlants.getValue())
                    : new ArrayList<>();

            if (netData != null) {
                // добавляем сетевые данные, избегая дубликатов
                for (Plant networkPlant : netData) {
                    boolean exists = false;
                    for (Plant existing : current) {
                        if (existing.getName().equalsIgnoreCase(networkPlant.getName())) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) current.add(networkPlant);
                }
                combinedPlants.setValue(current);
            }
        });
    }

    /** Возвращает объединённые данные для наблюдения из UI */
    public LiveData<List<Plant>> getPlants() {
        return combinedPlants;
    }

    /** Загружаем растения из локальной базы данных (Room) */
    public void loadFromDatabase() {
        repository.getFavoritePlantsFromDatabase(new PlantRepository.PlantDatabaseCallback() {
            @Override
            public void onSuccess(List<Plant> plants) {
                plantsFromDb.postValue(plants);
            }

            @Override
            public void onError(String error) {
                plantsFromDb.postValue(null);
            }
        });
    }

    /** Загружаем растения из mock API */
    public void loadFromNetwork(String query) {
        repository.searchPlants(query, new PlantRepository.PlantSearchCallback() {
            @Override
            public void onSuccess(List<Plant> plants) {
                plantsFromNetwork.postValue(plants);
            }

            @Override
            public void onError(String message) {
                plantsFromNetwork.postValue(null);
            }
        });
    }
}
