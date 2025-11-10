package ru.mirea.kirichenkoal.lesson9.data.repository;

import androidx.lifecycle.MutableLiveData;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.kirichenkoal.lesson9.data.network.ApiService;
import ru.mirea.kirichenkoal.lesson9.data.network.RetrofitClient;
import ru.mirea.kirichenkoal.lesson9.data.network.dto.PlantApiModel;

public class PlantNetworkRepository {
    private final ApiService api = RetrofitClient.getApiService();

    public void loadPlants(MutableLiveData<List<PlantApiModel>> data) {
        api.getTodos().enqueue(new Callback<List<PlantApiModel>>() {
            @Override
            public void onResponse(Call<List<PlantApiModel>> call, Response<List<PlantApiModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                } else {
                    data.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<PlantApiModel>> call, Throwable t) {
                data.postValue(null);
            }
        });
    }
}
