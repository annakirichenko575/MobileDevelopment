package ru.mirea.kirichenkoal.lesson9.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mirea.kirichenkoal.lesson9.data.repository.MockPlantRepository;
import ru.mirea.kirichenkoal.lesson9.data.repository.PlantNetworkRepository;
import ru.mirea.kirichenkoal.lesson9.data.network.dto.PlantApiModel;
import ru.mirea.kirichenkoal.lesson9.domain.models.PlantItem;

/**
 * ViewModel объединяет данные из Mock и из сети (Retrofit).
 * Используется на главном экране.
 */
public class PlantListViewModel extends ViewModel {

    // Репозиторий с локальными (mock) данными
    private final MockPlantRepository mockRepository = MockPlantRepository.getInstance();

    // Репозиторий для загрузки данных из сети
    private final PlantNetworkRepository networkRepository = new PlantNetworkRepository();

    // LiveData с локальными растениями
    public LiveData<List<PlantItem>> getPlants() {
        return mockRepository.getPlants();
    }

    // Добавление/удаление из избранного
    public void toggleFavorite(PlantItem item) {
        mockRepository.toggleFavorite(item);
    }

    // LiveData с растениями из сети (Retrofit)
    private final MutableLiveData<List<PlantApiModel>> networkPlants = new MutableLiveData<>();

    public LiveData<List<PlantApiModel>> getNetworkPlants() {
        return networkPlants;
    }

    // Метод для загрузки из API
    public void loadFromNetwork() {
        networkRepository.loadPlants(networkPlants);
    }
}
