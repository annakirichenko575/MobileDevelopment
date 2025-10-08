package ru.mirea.kirichenkoal.lesson9.data.storage.models;

public class StoragePlant {
    private int id;
    private String name;
    private String lastModified;

    public StoragePlant(int id, String name, String lastModified) {
        this.id = id;
        this.name = name;
        this.lastModified = lastModified;
    }

    // Геттеры
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastModified() {
        return lastModified;
    }

    // Сеттеры (если понадобятся)
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}