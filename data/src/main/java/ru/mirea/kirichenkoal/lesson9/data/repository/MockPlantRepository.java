package ru.mirea.kirichenkoal.lesson9.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.kirichenkoal.lesson9.domain.models.PlantItem;

public class MockPlantRepository {

    private static MockPlantRepository instance;

    private final MutableLiveData<List<PlantItem>> plantsLiveData = new MutableLiveData<>();
    private final List<PlantItem> plantList = new ArrayList<>();

    private MockPlantRepository() {
        if (plantList.isEmpty()) {
            plantList.add(new PlantItem("Роза", "Классический цветок любви", "rose"));
            plantList.add(new PlantItem("Орхидея", "Тропическое растение", "orchid"));
            plantList.add(new PlantItem("Кактус", "Минимум воды, максимум колючек", "cactus"));
            plantList.add(new PlantItem("Папоротник", "Любит влажность и тень", "fern"));
            plantList.add(new PlantItem("Алоэ", "Лечебное растение для кожи", "aloe"));
            plantList.add(new PlantItem("Фикус", "Популярное домашнее растение", "ficus"));
            plantList.add(new PlantItem("Монстера", "Экзотическое растение с большими листьями", "monstera"));
            plantList.add(new PlantItem("Бегония", "Яркое и пышное растение", "begonia"));
            plantList.add(new PlantItem("Сансевиерия", "Тещин язык, выносливое растение", "sansevieria"));
            plantList.add(new PlantItem("Лаванда", "Ароматное растение, снимает стресс", "lavender"));
            plantList.add(new PlantItem("Мята", "Освежающее и ароматное растение", "mint"));
        }
        plantsLiveData.setValue(new ArrayList<>(plantList));
    }

    public static synchronized MockPlantRepository getInstance() {
        if (instance == null) {
            instance = new MockPlantRepository();
        }
        return instance;
    }

    public LiveData<List<PlantItem>> getPlants() {
        plantsLiveData.setValue(new ArrayList<>(plantList));
        return plantsLiveData;
    }

    public void toggleFavorite(PlantItem item) {
        item.setFavorite(!item.isFavorite());
        plantsLiveData.setValue(new ArrayList<>(plantList));
    }

    public List<PlantItem> getFavorites() {
        List<PlantItem> favorites = new ArrayList<>();
        for (PlantItem p : plantList) {
            if (p.isFavorite()) favorites.add(p);
        }
        return favorites;
    }

    public List<PlantItem> searchPlants(String query) {
        List<PlantItem> result = new ArrayList<>();
        for (PlantItem plant : plantList) {
            if (plant.getName().toLowerCase().contains(query.toLowerCase())) {
                result.add(plant);
            }
        }
        return result;
    }
}

