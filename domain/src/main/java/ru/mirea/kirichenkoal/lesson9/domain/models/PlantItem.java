package ru.mirea.kirichenkoal.lesson9.domain.models;

public class PlantItem {
    private int id;                // Уникальный идентификатор (для адаптера)
    private String name;           // Название растения
    private String description;    // Описание
    private String imageName;      // Имя изображения (в drawable)
    private boolean isFavorite;    // Флаг избранного

    // 🔹 Конструктор с 3 параметрами (старый)
    public PlantItem(String name, String description, String imageName) {
        this.name = name;
        this.description = description;
        this.imageName = imageName;
        this.isFavorite = false;
    }

    // 🔹 Конструктор с 4 параметрами (для новых вызовов)
    public PlantItem(String name, String description, String imageName, boolean isFavorite) {
        this.name = name;
        this.description = description;
        this.imageName = imageName;
        this.isFavorite = isFavorite;
    }

    // 🔹 Конструктор с id (если в будущем понадобится база данных)
    public PlantItem(int id, String name, String description, String imageName, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageName = imageName;
        this.isFavorite = isFavorite;
    }

    // === Геттеры и сеттеры ===
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
