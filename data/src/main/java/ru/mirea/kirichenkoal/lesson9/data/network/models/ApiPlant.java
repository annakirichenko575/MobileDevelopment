package ru.mirea.kirichenkoal.lesson9.data.network.models;

public class ApiPlant {
    public int id;
    public String name;
    public String scientificName;
    public String family;
    public String description;
    public String careInstructions;
    public String imageUrl;

    public ApiPlant(int id, String name, String scientificName, String family,
                    String description, String careInstructions, String imageUrl) {
        this.id = id;
        this.name = name;
        this.scientificName = scientificName;
        this.family = family;
        this.description = description;
        this.careInstructions = careInstructions;
        this.imageUrl = imageUrl;
    }
}