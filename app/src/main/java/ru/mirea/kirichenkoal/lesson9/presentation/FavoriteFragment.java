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

import java.util.List;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.domain.models.PlantItem;
import ru.mirea.kirichenkoal.lesson9.presentation.viewmodel.FavoritePlantsViewModel;

public class FavoriteFragment extends Fragment {

    private FavoritePlantsViewModel viewModel;
    private PlantListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PlantListAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnFavoriteClickListener(plant ->
                Toast.makeText(requireContext(), "Избранное редактируется на главной странице", Toast.LENGTH_SHORT).show()
        );

        viewModel = new ViewModelProvider(this).get(FavoritePlantsViewModel.class);
        viewModel.getFavorites().observe(getViewLifecycleOwner(), this::displayFavorites);
    }

    private void displayFavorites(List<PlantItem> plants) {
        if (plants == null || plants.isEmpty()) {
            Toast.makeText(requireContext(), "Нет избранных растений", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setPlants(plants);
        }
    }
}
