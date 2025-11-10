package ru.mirea.kirichenkoal.lesson9.data.network;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.kirichenkoal.lesson9.data.network.dto.PlantInfoApiModel;

public class PlantInfoRepository {

    // ТВОЙ реальный базовый URL mockapi.io
    private static final String BASE_URL = "https://6911fb7652a60f10c82023a3.mockapi.io/api/v1/";
    private final PlantInfoApiService apiService;

    public PlantInfoRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(PlantInfoApiService.class);
    }

    // plantKey — это латиницей (imageName), например "rose"
    public void loadPlantInfo(String plantKey, MutableLiveData<PlantInfoApiModel> liveData) {
        apiService.getPlantInfo().enqueue(new Callback<List<PlantInfoApiModel>>() {
            @Override
            public void onResponse(Call<List<PlantInfoApiModel>> call, Response<List<PlantInfoApiModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (PlantInfoApiModel model : response.body()) {
                        if (model.getPlant() != null && model.getPlant().equalsIgnoreCase(plantKey)) {
                            liveData.postValue(model);
                            return;
                        }
                    }
                }
                liveData.postValue(null);
            }

            @Override
            public void onFailure(Call<List<PlantInfoApiModel>> call, Throwable t) {
                liveData.postValue(null);
            }
        });
    }
}
