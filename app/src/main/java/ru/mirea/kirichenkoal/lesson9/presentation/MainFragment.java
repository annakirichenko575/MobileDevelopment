package ru.mirea.kirichenkoal.lesson9.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.presentation.viewmodel.PlantListViewModel;

public class MainFragment extends Fragment {

    private PlantListViewModel viewModel;
    private PlantListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPlants);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PlantListAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnFavoriteClickListener(plant -> {
            viewModel.toggleFavorite(plant);
            Toast.makeText(requireContext(),
                    (plant.isFavorite() ? "Добавлено в избранное" : "Удалено из избранного"),
                    Toast.LENGTH_SHORT).show();
        });

        viewModel = new ViewModelProvider(this).get(PlantListViewModel.class);
        viewModel.getPlants().observe(getViewLifecycleOwner(), adapter::setPlants);

        Button buttonLoadNetwork = view.findViewById(R.id.buttonLoadFromNetwork);
        buttonLoadNetwork.setOnClickListener(v -> {
            viewModel.loadFromNetwork();
            Toast.makeText(requireContext(), "Загрузка растений из сети...", Toast.LENGTH_SHORT).show();
        });

        viewModel.getNetworkPlants().observe(getViewLifecycleOwner(), plants -> {
            if (plants != null) {
                adapter.setPlantsFromApi(plants);
                Toast.makeText(requireContext(), "Данные получены из сети!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Ошибка загрузки из сети", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
