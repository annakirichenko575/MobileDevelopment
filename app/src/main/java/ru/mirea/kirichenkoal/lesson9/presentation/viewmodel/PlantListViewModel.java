package ru.mirea.kirichenkoal.lesson9.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mirea.kirichenkoal.lesson9.data.repository.MockPlantRepository;
import ru.mirea.kirichenkoal.lesson9.domain.models.PlantItem;

public class PlantListViewModel extends ViewModel {
    private final MockPlantRepository repository = MockPlantRepository.getInstance();

    public LiveData<List<PlantItem>> getPlants() {
        return repository.getPlants();
    }

    public void toggleFavorite(PlantItem item) {
        repository.toggleFavorite(item);
    }
}
