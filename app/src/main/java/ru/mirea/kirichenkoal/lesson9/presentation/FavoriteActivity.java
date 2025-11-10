package ru.mirea.kirichenkoal.lesson9.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.domain.models.PlantItem;
import ru.mirea.kirichenkoal.lesson9.presentation.viewmodel.FavoritePlantsViewModel;
import android.content.Intent;
import android.widget.Toast;
import ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthManager;
import ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthActivity;

/**
 * Экран "Избранное"
 * Отображает растения, добавленные в избранное из общего MockPlantRepository.
 */
public class FavoriteActivity extends AppCompatActivity {

    private FavoritePlantsViewModel viewModel;
    private PlantListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        if (ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthManager.isGuest()) {
            android.widget.Toast.makeText(this, "Войдите, чтобы просматривать избранное", android.widget.Toast.LENGTH_SHORT).show();
            startActivity(new android.content.Intent(this, ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthActivity.class));
            finish();
            return;
        }

        setupRecyclerView();
        setupViewModel();
        setupBottomNavigation();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlantListAdapter();
        recyclerView.setAdapter(adapter);

        // кнопка избранного в этом списке неактивна
        adapter.setOnFavoriteClickListener(plant ->
                Toast.makeText(this, "Избранное редактируется на главной странице", Toast.LENGTH_SHORT).show());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(FavoritePlantsViewModel.class);
        viewModel.getFavorites().observe(this, this::displayFavorites);
    }

    private void displayFavorites(List<PlantItem> plants) {
        if (plants == null || plants.isEmpty()) {
            Toast.makeText(this, "Нет избранных растений", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setPlants(plants);
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_favorite);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;

            } else if (id == R.id.navigation_search) {
                startActivity(new Intent(this, SearchActivity.class));
                overridePendingTransition(0, 0);
                return true;

            } else if (id == R.id.navigation_analyze) {
                Toast.makeText(this, "Функция анализа листа пока не реализована", Toast.LENGTH_SHORT).show();
                return true;

            } else if (id == R.id.navigation_favorite) {
                // Уже здесь. Если гость — мягко предложим авторизоваться.
                if (ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthManager.isGuest()) {
                    startActivity(new Intent(this, ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthActivity.class));
                }
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
