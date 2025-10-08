package ru.mirea.kirichenkoal.lesson9.data.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ru.mirea.kirichenkoal.lesson9.data.database.entity.PlantEntity;

@Dao
public interface PlantDao {

    @Insert
    long insertPlant(PlantEntity plant);

    @Update
    void updatePlant(PlantEntity plant);

    @Query("SELECT * FROM plants WHERE isFavorite = 1")
    List<PlantEntity> getFavoritePlants();

    @Query("SELECT * FROM plants WHERE id = :id")
    PlantEntity getPlantById(int id);

    @Query("SELECT * FROM plants ORDER BY createdAt DESC LIMIT :limit")
    List<PlantEntity> getRecentPlants(int limit);

    @Query("DELETE FROM plants WHERE id = :id")
    void deletePlant(int id);
}