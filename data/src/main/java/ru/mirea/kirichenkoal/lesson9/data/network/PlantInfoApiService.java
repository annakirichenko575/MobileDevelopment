package ru.mirea.kirichenkoal.lesson9.data.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.mirea.kirichenkoal.lesson9.data.network.dto.PlantInfoApiModel;

public interface PlantInfoApiService {
    @GET("plantinfo")
    Call<List<PlantInfoApiModel>> getPlantInfo();
}
