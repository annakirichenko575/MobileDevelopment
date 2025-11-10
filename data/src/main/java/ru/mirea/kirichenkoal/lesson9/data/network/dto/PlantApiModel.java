package ru.mirea.kirichenkoal.lesson9.data.network.dto;

import com.google.gson.annotations.SerializedName;

public class PlantApiModel {

    @SerializedName("userId")
    private Integer userId;

    @SerializedName("id")
    private Integer id;

    @SerializedName("title")
    private String title;

    @SerializedName("completed")
    private Boolean completed;

    public Integer getUserId() { return userId; }
    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public Boolean getCompleted() { return completed; }
}
