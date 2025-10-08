package ru.mirea.kirichenkoal.lesson9.data.storage.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import ru.mirea.kirichenkoal.lesson9.data.storage.PlantStorage;
import ru.mirea.kirichenkoal.lesson9.data.storage.models.StoragePlant;

public class SharedPrefPlantStorage implements PlantStorage {

    private static final String PREFS_NAME = "plant_storage";
    private static final String KEY_FAVORITE_ID = "favorite_plant_id";
    private static final String KEY_FAVORITE_NAME = "favorite_plant_name";
    private static final String KEY_LAST_MODIFIED = "favorite_plant_modified";
    private static final String KEY_FAVORITES_SET = "favorites_set";

    private final SharedPreferences prefs;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public SharedPrefPlantStorage(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public StoragePlant getFavoritePlant() {
        int id = prefs.getInt(KEY_FAVORITE_ID, -1);
        String name = prefs.getString(KEY_FAVORITE_NAME, "");
        String lastModified = prefs.getString(KEY_LAST_MODIFIED, "");

        if (id == -1 || name.isEmpty()) {
            return new StoragePlant(1, "Растение по умолчанию", getCurrentDateTime());
        }

        return new StoragePlant(id, name, lastModified);
    }

    @Override
    public boolean saveFavoritePlant(StoragePlant plant) {
        if (plant == null || plant.getName() == null) return false;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_FAVORITE_ID, plant.getId());
        editor.putString(KEY_FAVORITE_NAME, plant.getName());
        editor.putString(KEY_LAST_MODIFIED, getCurrentDateTime());
        return editor.commit();
    }

    @Override
    public void addToFavorites(String plantId) {
        Set<String> favorites = new HashSet<>(prefs.getStringSet(KEY_FAVORITES_SET, new HashSet<>()));
        favorites.add(plantId);
        prefs.edit().putStringSet(KEY_FAVORITES_SET, favorites).apply();
    }

    @Override
    public void removeFromFavorites(String plantId) {
        Set<String> favorites = new HashSet<>(prefs.getStringSet(KEY_FAVORITES_SET, new HashSet<>()));
        if (favorites.remove(plantId)) {
            prefs.edit().putStringSet(KEY_FAVORITES_SET, favorites).apply();
        }
    }

    @Override
    public Set<String> getFavoriteIds() {
        return prefs.getStringSet(KEY_FAVORITES_SET, new HashSet<>());
    }

    private String getCurrentDateTime() {
        return LocalDateTime.now().format(formatter);
    }
}