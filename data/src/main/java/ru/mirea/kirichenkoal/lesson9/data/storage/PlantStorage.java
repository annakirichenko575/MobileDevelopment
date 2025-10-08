package ru.mirea.kirichenkoal.lesson9.data.storage;

import ru.mirea.kirichenkoal.lesson9.data.storage.models.StoragePlant;
import java.util.Set;

public interface PlantStorage {
    // Методы для работы с основным любимым растением
    StoragePlant getFavoritePlant();
    boolean saveFavoritePlant(StoragePlant plant);

    // Методы для работы со списком избранных растений
    void addToFavorites(String plantId);
    void removeFromFavorites(String plantId);
    Set<String> getFavoriteIds();
}