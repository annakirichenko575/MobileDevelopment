package ru.mirea.kirichenkoal.lesson9.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.data.repository.PlantRepositoryImpl;
import ru.mirea.kirichenkoal.lesson9.domain.models.Plant;
import ru.mirea.kirichenkoal.lesson9.domain.repository.PlantRepository;
import ru.mirea.kirichenkoal.lesson9.domain.usecases.SearchPlantsUseCase;

public class SearchActivity extends AppCompatActivity {

    private TextInputEditText etSearch;
    private MaterialButton btnSearch;
    private View progressBar;
    private RecyclerView recyclerViewResults;
    private View tvResultsTitle, tvNoResults;

    private PlantAdapter adapter;
    private PlantRepository plantRepository;
    private SearchPlantsUseCase searchUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        setupDependencies();
        setupClickListeners();
    }

    private void initViews() {
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        progressBar = findViewById(R.id.progressBar);
        recyclerViewResults = findViewById(R.id.recyclerViewResults);
        tvResultsTitle = findViewById(R.id.tvResultsTitle);
        tvNoResults = findViewById(R.id.tvNoResults);

        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlantAdapter();
        recyclerViewResults.setAdapter(adapter);
    }

    private void setupDependencies() {
        plantRepository = new PlantRepositoryImpl(this);
        searchUseCase = new SearchPlantsUseCase(plantRepository);
    }

    private void setupClickListeners() {
        btnSearch.setOnClickListener(v -> performSearch());
    }

    private void performSearch() {
        String query = etSearch.getText().toString().trim();

        if (query.isEmpty()) {
            Toast.makeText(this, "Введите поисковый запрос", Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);
        hideResults();

        searchUseCase.execute(query, new PlantRepository.PlantSearchCallback() {
            @Override
            public void onSuccess(List<Plant> plants) {
                runOnUiThread(() -> {
                    setLoading(false);
                    displayResults(plants);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    setLoading(false);
                    Toast.makeText(SearchActivity.this, message, Toast.LENGTH_LONG).show();
                    showNoResults();
                });
            }
        });
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSearch.setEnabled(!loading);
        btnSearch.setText(loading ? "Поиск..." : "Найти");
    }

    private void displayResults(List<Plant> plants) {
        if (plants.isEmpty()) {
            showNoResults();
        } else {
            showResults();
            adapter.setPlants(plants);
        }
    }

    private void showResults() {
        tvResultsTitle.setVisibility(View.VISIBLE);
        recyclerViewResults.setVisibility(View.VISIBLE);
        tvNoResults.setVisibility(View.GONE);
    }

    private void showNoResults() {
        tvResultsTitle.setVisibility(View.GONE);
        recyclerViewResults.setVisibility(View.GONE);
        tvNoResults.setVisibility(View.VISIBLE);
    }

    private void hideResults() {
        tvResultsTitle.setVisibility(View.GONE);
        recyclerViewResults.setVisibility(View.GONE);
        tvNoResults.setVisibility(View.GONE);
    }
}
