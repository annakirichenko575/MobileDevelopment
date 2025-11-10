package ru.mirea.kirichenkoal.lesson9.data.network;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import ru.mirea.kirichenkoal.lesson9.data.network.dto.PlantApiModel;

public interface ApiService {
    @GET("todos")
    Call<List<PlantApiModel>> getTodos();
}
