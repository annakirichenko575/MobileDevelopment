package ru.mirea.kirichenkoal.lesson9.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.domain.models.PlantItem;
import ru.mirea.kirichenkoal.lesson9.presentation.viewmodel.PlantListViewModel;
import ru.mirea.kirichenkoal.lesson9.data.network.dto.PlantApiModel;

/**
 * Главный экран приложения — отображает растения из локального и сетевого источников.
 */
public class MainActivity extends AppCompatActivity {

    private PlantListViewModel viewModel;
    private PlantListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecyclerView();
        setupViewModel();
        setupBottomNavigation();

        // 1) Обработчик кнопки "Загрузить растения из сети"
        Button buttonLoadNetwork = findViewById(R.id.buttonLoadFromNetwork);
        buttonLoadNetwork.setOnClickListener(v -> {
            viewModel.loadFromNetwork();
            Toast.makeText(this, "Загрузка растений из сети...", Toast.LENGTH_SHORT).show();
        });

        // 2) Подписка на сетевые данные и отображение их в списке
        viewModel.getNetworkPlants().observe(this, plants -> {
            if (plants != null) {
                adapter.setPlantsFromApi(plants);  // этот метод мы добавили в адаптер
                Toast.makeText(this, "Данные получены из сети!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ошибка загрузки из сети", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPlants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlantListAdapter();
        recyclerView.setAdapter(adapter);

        // обработчик нажатий на кнопку "В избранное"
        adapter.setOnFavoriteClickListener(plant -> {
            viewModel.toggleFavorite(plant);
            Toast.makeText(this,
                    (plant.isFavorite() ? "Добавлено в избранное" : "Удалено из избранного"),
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(PlantListViewModel.class);
        // Подписка на локальные данные (mock)
        viewModel.getPlants().observe(this, adapter::setPlants);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home); // текущая вкладка

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                return true; // уже на главной

            } else if (id == R.id.navigation_search) {
                startActivity(new Intent(this, SearchActivity.class));
                overridePendingTransition(0, 0);
                return true;

            } else if (id == R.id.navigation_analyze) {
                Toast.makeText(this, "Функция анализа листа пока не реализована", Toast.LENGTH_SHORT).show();
                return true;

            } else if (id == R.id.navigation_favorite) {
                if (ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthManager.isGuest()) {
                    startActivity(new Intent(this, ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthActivity.class));
                    return true;
                }
                startActivity(new Intent(this, FavoriteActivity.class));
                overridePendingTransition(0, 0);
                return true;

            } else if (id == R.id.navigation_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });
    }
}
