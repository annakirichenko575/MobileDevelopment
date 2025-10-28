package ru.mirea.kirichenkoal.lesson9.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mirea.kirichenkoal.lesson9.data.repository.MockPlantRepository;
import ru.mirea.kirichenkoal.lesson9.domain.models.PlantItem;

public class FavoritePlantsViewModel extends ViewModel {
    private final MockPlantRepository repository = MockPlantRepository.getInstance();
    private final MutableLiveData<List<PlantItem>> favoritesLiveData = new MutableLiveData<>();

    public LiveData<List<PlantItem>> getFavorites() {
        favoritesLiveData.setValue(repository.getFavorites());
        return favoritesLiveData;
    }
}
