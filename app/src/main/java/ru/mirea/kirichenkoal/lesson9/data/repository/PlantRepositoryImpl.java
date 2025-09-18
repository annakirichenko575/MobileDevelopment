package ru.mirea.kirichenkoal.lesson9.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.mirea.kirichenkoal.lesson9.domain.models.Plant;
import ru.mirea.kirichenkoal.lesson9.domain.repository.PlantRepository;

public class PlantRepositoryImpl implements PlantRepository {

    private static final String PREFS_NAME = "plant_prefs";
    private static final String KEY_ID = "favorite_plant_id";
    private static final String KEY_NAME = "favorite_plant_name";
    private static final String KEY_FAVORITES = "favorites_set";

    private final SharedPreferences prefs;
    private final List<Plant> demoPlants = new ArrayList<>();

    public PlantRepositoryImpl(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        demoPlants.add(new Plant(1, "Фикус"));
        demoPlants.add(new Plant(2, "Алоэ"));
        demoPlants.add(new Plant(3, "Монстера"));
    }

    @Override
    public boolean savePlant(Plant plant) {
        if (plant == null || plant.getName() == null) return false;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_ID, plant.getId());
        editor.putString(KEY_NAME, plant.getName());
        editor.apply();
        return true;
    }

    @Override
    public Plant getFavoritePlant() {
        int id = prefs.getInt(KEY_ID, -1);
        String name = prefs.getString(KEY_NAME, null);
        if (id == -1 || name == null) {
            return new Plant(1, "Doctor Strange");
        }
        return new Plant(id, name);
    }

    @Override
    public void addPlantsToFavorityByID(String plantId) {
        Set<String> set = new HashSet<>(prefs.getStringSet(KEY_FAVORITES, new HashSet<String>()));
        set.add(plantId);
        prefs.edit().putStringSet(KEY_FAVORITES, set).apply();
    }

    @Override
    public void removePlantsFromFavorityByID(String plantId) {
        Set<String> set = new HashSet<>(prefs.getStringSet(KEY_FAVORITES, new HashSet<String>()));
        if (set.remove(plantId)) {
            prefs.edit().putStringSet(KEY_FAVORITES, set).apply();
        }
    }

    @Override
    public List<Plant> getFavorityPlantByPage(int limit, int offset) {
        Set<String> set = prefs.getStringSet(KEY_FAVORITES, new HashSet<String>());
        List<String> ids = new ArrayList<>(set);
        List<Plant> out = new ArrayList<>();
        int start = Math.min(offset, ids.size());
        int end = Math.min(offset + limit, ids.size());
        for (int i = start; i < end; i++) {
            String idStr = ids.get(i);
            try {
                int id = Integer.parseInt(idStr);
                Plant p = findDemoPlantById(id);
                if (p != null) out.add(p);
            } catch (NumberFormatException ignored) {}
        }
        return out;
    }

    private Plant findDemoPlantById(int id) {
        for (Plant p : demoPlants) {
            if (p.getId() == id) return p;
        }
        return null;
    }
}
