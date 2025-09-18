package ru.mirea.kirichenkoal.lesson9.domain.models;

public class Plant {
    private int id;
    private String name;

    public Plant(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}
