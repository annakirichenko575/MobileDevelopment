package ru.mirea.kirichenkoal.lesson9.domain.models;

public class PlantItem {
    private final String name;
    private final String description;
    private final String imageName;
    private boolean isFavorite;

    public PlantItem(String name, String description, String imageName) {
        this.name = name;
        this.description = description;
        this.imageName = imageName;
        this.isFavorite = false;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImageName() { return imageName; }
    public boolean isFavorite() { return isFavorite; }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }
}
