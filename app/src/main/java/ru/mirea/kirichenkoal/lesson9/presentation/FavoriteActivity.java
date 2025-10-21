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
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.data.repository.PlantRepositoryImpl;
import ru.mirea.kirichenkoal.lesson9.domain.models.Plant;
import ru.mirea.kirichenkoal.lesson9.domain.usecases.GetFavorityPlantByPage;
import ru.mirea.kirichenkoal.lesson9.domain.usecases.RemovePlantsFromFavorityByID;
import ru.mirea.kirichenkoal.lesson9.presentation.viewmodel.PlantViewModel;
import ru.mirea.kirichenkoal.lesson9.presentation.viewmodel.PlantViewModelFactory;

public class FavoriteActivity extends AppCompatActivity {

    private ListView listView;
    private PlantRepositoryImpl repo;
    private GetFavorityPlantByPage getFavUseCase;
    private RemovePlantsFromFavorityByID removeUseCase;
    private List<Plant> items = new ArrayList<>();

    // Добавляем ViewModel
    private PlantViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        listView = findViewById(R.id.listViewFavorites);

        // Создаём репозиторий
        repo = new PlantRepositoryImpl(this);
        getFavUseCase = new GetFavorityPlantByPage(repo);
        removeUseCase = new RemovePlantsFromFavorityByID(repo);

        // === Создаём ViewModel ===
        PlantViewModelFactory factory = new PlantViewModelFactory(repo);
        viewModel = new ViewModelProvider(this, factory).get(PlantViewModel.class);

        // Настраиваем адаптер
        FavoriteAdapter adapter = new FavoriteAdapter();
        listView.setAdapter(adapter);

        // === Наблюдаем за LiveData ===
        viewModel.getPlants().observe(this, plants -> {
            if (plants != null) {
                items.clear();
                items.addAll(plants);
                adapter.notifyDataSetChanged();
            }
        });

        // === Загружаем данные из БД через ViewModel ===
        viewModel.loadFromDatabase();

        // === Кнопка "Назад" ===
        Button buttonBack = findViewById(R.id.buttonBackToMain);
        buttonBack.setOnClickListener(v -> finish());
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
            btnRemove.setOnClickListener(v -> {
                removeUseCase.execute(String.valueOf(plant.getId()));
                viewModel.loadFromDatabase(); // обновляем список через ViewModel
            });

            return convertView;
        }
    }
}
