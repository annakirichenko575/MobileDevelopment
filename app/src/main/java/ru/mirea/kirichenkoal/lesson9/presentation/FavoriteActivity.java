package ru.mirea.kirichenkoal.lesson9.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    // ViewModel
    private PlantViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        listView = findViewById(R.id.listViewFavorites);

        // Репозиторий и use case
        repo = new PlantRepositoryImpl(this);
        getFavUseCase = new GetFavorityPlantByPage(repo);
        removeUseCase = new RemovePlantsFromFavorityByID(repo);

        // ViewModel
        PlantViewModelFactory factory = new PlantViewModelFactory(repo);
        viewModel = new ViewModelProvider(this, factory).get(PlantViewModel.class);

        // Настройка адаптера
        FavoriteAdapter adapter = new FavoriteAdapter();
        listView.setAdapter(adapter);

        // Подписка на LiveData
        viewModel.getPlants().observe(this, plants -> {
            if (plants != null) {
                items.clear();
                items.addAll(plants);
                adapter.notifyDataSetChanged();
            }
        });

        // Загрузка данных
        viewModel.loadFromDatabase();

        // === Нижнее меню ===
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_favorite);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.navigation_search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            } else if (id == R.id.navigation_analyze) {
                Toast.makeText(this, "Функция анализа листа пока не реализована", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.navigation_favorite) {
                return true; // уже здесь
            } else if (id == R.id.navigation_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private class FavoriteAdapter extends BaseAdapter {
        @Override
        public int getCount() { return items.size(); }

        @Override
        public Object getItem(int i) { return items.get(i); }

        @Override
        public long getItemId(int i) { return items.get(i).getId(); }

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
                viewModel.loadFromDatabase();
            });

            return convertView;
        }
    }
}
