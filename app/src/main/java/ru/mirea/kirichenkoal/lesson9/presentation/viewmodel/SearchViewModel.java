package ru.mirea.kirichenkoal.lesson9.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mirea.kirichenkoal.lesson9.data.repository.MockPlantRepository;
import ru.mirea.kirichenkoal.lesson9.domain.models.PlantItem;

public class SearchViewModel extends ViewModel {
    private final MockPlantRepository repository = MockPlantRepository.getInstance();

    private final MutableLiveData<List<PlantItem>> searchResults = new MutableLiveData<>();

    public LiveData<List<PlantItem>> getResults() {
        return searchResults;
    }

    public void search(String query) {
        if (query == null || query.trim().isEmpty()) {
            searchResults.setValue(null);
            return;
        }

        List<PlantItem> results = repository.searchPlants(query);
        searchResults.setValue(results);
    }
}
