package ru.mirea.kirichenkoal.lesson9.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.ArrayList;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.data.repository.PlantRepositoryImpl;
import ru.mirea.kirichenkoal.lesson9.domain.models.Plant;
import ru.mirea.kirichenkoal.lesson9.domain.usecases.GetFavorityPlantByPage;
import ru.mirea.kirichenkoal.lesson9.domain.usecases.RemovePlantsFromFavorityByID;

public class FavoriteActivity extends AppCompatActivity {

    private ListView listView;
    private PlantRepositoryImpl repo;
    private GetFavorityPlantByPage getFavUseCase;
    private RemovePlantsFromFavorityByID removeUseCase;
    private List<Plant> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        listView = findViewById(R.id.listViewFavorites);

        repo = new PlantRepositoryImpl(this);
        getFavUseCase = new GetFavorityPlantByPage(repo);
        removeUseCase = new RemovePlantsFromFavorityByID(repo);

        loadAndShow();

        // === Кнопка "Назад" ===
        Button buttonBack = findViewById(R.id.buttonBackToMain);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // закрываем FavoriteActivity и возвращаемся на MainActivity
            }
        });
    }

    private void loadAndShow() {
        repo.getFavoritePlantsFromDatabase(new PlantRepositoryImpl.PlantDatabaseCallback() {
            @Override
            public void onSuccess(List<Plant> plants) {
                items = plants;
                if (items == null) {
                    items = new ArrayList<>();
                }
                FavoriteAdapter adapter = new FavoriteAdapter();
                listView.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {
                items = new ArrayList<>();
            }
        });
    }


    private class FavoriteAdapter extends BaseAdapter {
        @Override public int getCount() { return items.size(); }
        @Override public Object getItem(int i) { return items.get(i); }
        @Override public long getItemId(int i) { return items.get(i).getId(); }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(FavoriteActivity.this)
                        .inflate(R.layout.favorite_item, parent, false);
            }
            TextView tvName = convertView.findViewById(R.id.textPlantName);
            Button btnRemove = convertView.findViewById(R.id.buttonRemove);

            Plant plant = items.get(position);
            tvName.setText(plant.getName());
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeUseCase.execute(String.valueOf(plant.getId()));
                    loadAndShow();
                }
            });

            return convertView;
        }
    }
}
