package ru.mirea.kirichenkoal.lesson9.data.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "plants")
public class PlantEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String type;
    public String description;
    public long createdAt;
    public boolean isFavorite;

    public PlantEntity(String name, String type, String description, long createdAt, boolean isFavorite) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.createdAt = createdAt;
        this.isFavorite = isFavorite;
    }
}