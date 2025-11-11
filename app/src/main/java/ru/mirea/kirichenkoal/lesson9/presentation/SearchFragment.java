package ru.mirea.kirichenkoal.lesson9.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.presentation.viewmodel.SearchViewModel;

public class SearchFragment extends Fragment {

    private TextInputEditText etSearch;
    private View progressBar, tvResultsTitle, tvNoResults;
    private RecyclerView recyclerViewResults;
    private PlantListAdapter adapter;
    private SearchViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearch = view.findViewById(R.id.etSearch);
        progressBar = view.findViewById(R.id.progressBar);
        tvResultsTitle = view.findViewById(R.id.tvResultsTitle);
        tvNoResults = view.findViewById(R.id.tvNoResults);
        recyclerViewResults = view.findViewById(R.id.recyclerViewResults);

        recyclerViewResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PlantListAdapter();
        recyclerViewResults.setAdapter(adapter);

        adapter.setOnFavoriteClickListener(plant ->
                Toast.makeText(requireContext(),
                        (plant.isFavorite() ? "Удалено из избранного" : "Добавлено в избранное"),
                        Toast.LENGTH_SHORT).show());

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        viewModel.getResults().observe(getViewLifecycleOwner(), plants -> {
            setLoading(false);
            if (plants == null) {
                hideResults();
            } else if (plants.isEmpty()) {
                showNoResults();
            } else {
                showResults();
                adapter.setPlants(plants);
            }
        });

        MaterialButton btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> performSearch());
    }

    private void performSearch() {
        String query = etSearch.getText() != null ? etSearch.getText().toString().trim() : "";
        if (query.isEmpty()) {
            Toast.makeText(requireContext(), "Введите поисковый запрос", Toast.LENGTH_SHORT).show();
            return;
        }
        setLoading(true);
        hideResults();
        viewModel.search(query);
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        View btn = getView().findViewById(R.id.btnSearch);
        btn.setEnabled(!loading);
        if (btn instanceof com.google.android.material.button.MaterialButton) {
            ((com.google.android.material.button.MaterialButton) btn).setText(loading ? "Поиск..." : "Найти");
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
