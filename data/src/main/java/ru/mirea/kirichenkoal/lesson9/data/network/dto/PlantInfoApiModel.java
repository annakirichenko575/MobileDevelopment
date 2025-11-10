package ru.mirea.kirichenkoal.lesson9.data.network.dto;

import com.google.gson.annotations.SerializedName;

public class PlantInfoApiModel {

    @SerializedName("plant")
    private String plant;

    @SerializedName("watering")
    private String watering;

    @SerializedName("temperature")
    private String temperature;

    @SerializedName("humidity")
    private String humidity;

    public String getPlant() {
        return plant;
    }

    public String getWatering() {
        return watering;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }
}
